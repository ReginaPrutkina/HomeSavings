package project;

import businessLogicClasses.PercentTypeFactory;
import businessLogicClasses.TypeOfPercent;
import dataClasses.Deposit;
import dataClasses.User;
import log.Logging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import services.*;
import services.BDServices.UserDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Path("/api")
public class RestAPIAuth {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private Logging logging;

    @Autowired
    PercentTypeFactory percentTypeFactory;

    @Autowired
    UserService userService;

    @Autowired
    CommonAnswer commonAnswer;

    @Autowired
    Security security;

    public Logging getLogging() {
        return logging;
    }

    public void setLogging(Logging logging) {
        this.logging = logging;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GET
    @Path("/authUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity<CommonAnswer> authUser(
            @QueryParam(value = "login") String login,
            @QueryParam(value = "password") String password){
            User user = userService.userAuth(login,password);
            commonAnswer.clear();
        if (user == null) {
            commonAnswer.setErrorText("Клиент не прошел авторизацию");
            return new ResponseEntity<>(commonAnswer,  HttpStatus.NOT_FOUND);
        }
        //Генерим и записываем в секьюрити мапу уникальный ид сессии, передаем его в ответе
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.setUser(user);
        commonAnswer.setSessionUID(security.generateAndAddUID(commonRequest));
        commonAnswer.setUser(user);
        return new ResponseEntity<>(commonAnswer,  HttpStatus.OK);
    }

// Запрос данных клиента по ID доступен только админу
//обязательные поля: UID сессии, User:ID клиента
    @POST
    @Path("/userDepositsById")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity<CommonAnswer> getUserDeposits(CommonRequest request){
        int sessionUID = request.getSessionUID();
        commonAnswer.clear();
        //Проверка наличия сессии в мапе секьюрити
        if (!isSessionUIDValid(sessionUID)) {
            badSessionAnswer(sessionUID);
            return new ResponseEntity<>(commonAnswer,  HttpStatus.NOT_FOUND);
        }
        logging.setUserName(security.getUIDMap().get(sessionUID).getUser().getLogin());
        logging.log("REST-запрос Поиск данных клиента по ID: "+ request.getUser().getId());
        // проверка на админа
        if (!security.getUIDMap().get(sessionUID).getUser().getRole().equals("admin"))
        {
            commonAnswer.setErrorText("Пользователь не является администратором. " +
                    "Нет прав для получения данных клиента по ID.");
            logging.log(commonAnswer.getErrorText());
            return new ResponseEntity<>(commonAnswer,  HttpStatus.FORBIDDEN);
        }
        User user = userDAO.findUserById(request.getUser().getId());
        if (user != null)
            logging.log("Успешный ответ для " + user.toString());
        else
            logging.log("Данные не найдены");
        commonAnswer.setUser(user);
        //обновляем lastUpdate  сессии в мапе
        security.getUIDMap().get(sessionUID).setLastUpdate(new Date());
        return new ResponseEntity<>(commonAnswer,  HttpStatus.OK);
    }

    // Просмотр всех пользователей доступен только администратору
    //обязательные поля: UID сессии
    @POST
    @Path("/showUsers")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity<List<?>> showUsers(CommonRequest request){
        List<CommonAnswer> commonAnswers = new ArrayList<>();
        int sessionUID = request.getSessionUID();
        //Проверка наличия сессии в мапе секьюрити
        if (!isSessionUIDValid(sessionUID)) {
            badSessionAnswer(sessionUID);
            commonAnswers.add(commonAnswer);
            return new ResponseEntity<>(commonAnswers,  HttpStatus.NOT_FOUND);
        }
        logging.setUserName(security.getUIDMap().get(sessionUID).getUser().getLogin());
        logging.log("REST-запрос Вывод данных по всем клиентам из БД. ");
        // проверка на админа
        if (!security.getUIDMap().get(sessionUID).getUser().getRole().equals("admin"))
            {
                commonAnswer.clear();
                commonAnswer.setErrorText("Пользователь не является администратором. " +
                        "Нет прав для получения данных по всем клиентам.");
                logging.log(commonAnswer.getErrorText());
                commonAnswers.add(commonAnswer);
                return new ResponseEntity<>(commonAnswers,  HttpStatus.FORBIDDEN);
            }
        List<User> users = userDAO.findAll();
        //обновляем lastUpdate  сессии в мапе
        security.getUIDMap().get(sessionUID).setLastUpdate(new Date());
        logging.log("Успешный ответ. Найдено " + users.size() + " клиентов.");
        return new ResponseEntity<>(users,  HttpStatus.OK);
    }

    // авторизация не требуется. Справочная информация
    @GET
    @Path("/typeOfPercent")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity<String> getTypeOfPercent(@QueryParam(value = "type") int type){
        TypeOfPercent typeOfPercent = percentTypeFactory.getTypeOfPercentMap().get(type);
        if (typeOfPercent == null)
            return new ResponseEntity<>("Неверный TypeOfPercent: "+ type,  HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(typeOfPercent.toString(), HttpStatus.OK);
    }

    @PUT
    @Path("/newUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity<CommonAnswer> registerUser(User user){
        User newUser = userService.registerUser(user);
        commonAnswer.clear();
        if (newUser == null){
            commonAnswer.setErrorText("Клиент не прошел валидацию. Регистрация не успешна. ");
            return new ResponseEntity<>(commonAnswer,  HttpStatus.FORBIDDEN);
        }
            //Генерим и записываем в секьюрити мапу уникальный ид сессии, передаем его в ответе
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.setUser(user);
        commonAnswer.setSessionUID(security.generateAndAddUID(commonRequest));
        commonAnswer.setUser(newUser);
        return new ResponseEntity<>(commonAnswer,  HttpStatus.OK);
    }

//  удаление пользователей разрешено только для админа
//обязательные поля: UID сессии, User:ID клиента
    @DELETE
    @Path("/deleteUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity<CommonAnswer> deleteUser(CommonRequest request){
        int sessionUID = request.getSessionUID();
        commonAnswer.clear();
        //Проверка наличия сессии в мапе секьюрити
        if (!isSessionUIDValid(sessionUID)) {
            badSessionAnswer(sessionUID);
            return new ResponseEntity<>(commonAnswer,  HttpStatus.NOT_FOUND);
        }
        logging.setUserName(security.getUIDMap().get(sessionUID).getUser().getLogin());
        logging.log("REST-запрос Удаление данных клиента по ID: "+ request.getUser().getId());
        // проверка на админа
        if (!security.getUIDMap().get(sessionUID).getUser().getRole().equals("admin"))
        {
            commonAnswer.setErrorText("Пользователь не является администратором. " +
                    "Нет прав для удаления данных клиента по ID.");
            logging.log(commonAnswer.getErrorText());
            return new ResponseEntity<>(commonAnswer,  HttpStatus.FORBIDDEN);
        }
        User user = userDAO.findUserById(request.getUser().getId());
        if (user == null) {
            logging.log("Данные не найдены");
            commonAnswer.setErrorText("Не найден пользователь по ID "+ request.getUser().getId());
            return new ResponseEntity<>(commonAnswer,  HttpStatus.NOT_FOUND);
        }
        userDAO.delete(user);
        commonAnswer.setErrorText("Удален клиент " + user.toString());
        logging.log(commonAnswer.getErrorText());
        return new ResponseEntity<>(commonAnswer,  HttpStatus.OK);
    }

    //обновляет депозиты по указанным ID, добавляет депозиты, если ID  не указан
    //проверяем, что обновляемые депозиты  (с  ID) - принадлежат клиенту
    //обязательные поля - UID сессии, User: список депозитов
    @POST
    @Path("/updateUserDeposits")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity<CommonAnswer> updateUserDeposits(CommonRequest request){
        int sessionUID = request.getSessionUID();
        commonAnswer.clear();
        //Проверка наличия сессии в мапе секьюрити
        if (!isSessionUIDValid(sessionUID)) {
            badSessionAnswer(sessionUID);
            return new ResponseEntity<>(commonAnswer,  HttpStatus.NOT_FOUND);
        }
        //сохраняем в ответе сессию
        commonAnswer.setSessionUID(sessionUID);
        //Берем клиента из map-ы
        User user = security.getUIDMap().get(sessionUID).getUser();
        logging.setUserName(user.getLogin());
        logging.log("REST-запрос Обновление депозитов клиента : "+ user.getLogin());

        //Проверяем, принадлежали ли клиенту депозиты с указанными ID в request
        for (Deposit deposit: request.getUser().getDeposits()) {
            deposit.setUser(user);
            if (deposit.getId() != 0 && !containsId(deposit.getId(),user.getDeposits())) {
                commonAnswer.setErrorText("Депозит с ID " + deposit.getId() +
                        " не принадлежит клиенту " + user.toString());
                logging.log(commonAnswer.getErrorText());
                return new ResponseEntity<>(commonAnswer, HttpStatus.FORBIDDEN);
            }
        }
       // Привязываем клиенту новый список депозитов из request - a
        user.setDeposits(request.getUser().getDeposits());
        //сохраняем в БД
        userDAO.merge(user);
        user = userDAO.findUserById(user.getId());
        // обновляем данные клиента в  мапе сессий
        security.getUIDMap().get(sessionUID).getUser().setDeposits(user.getDeposits());
        //обновляем lastUpdate  сессии в мапе
        security.getUIDMap().get(sessionUID).setLastUpdate(new Date());

        logging.log("Депозиты клиента обновлены");
        commonAnswer.setUser(user);
        return new ResponseEntity<>(commonAnswer,  HttpStatus.OK);

    }

    private boolean containsId(int id, List<Deposit> deposits){
        for (Deposit deposit: deposits ) {
            if (id == deposit.getId())
                return true;
        }
        return false;
    }

   private boolean isSessionUIDValid(Integer sessionUID){
        if (sessionUID==null || sessionUID==0)
            return false;
        return security.containsUID(sessionUID) && !security.isSessionOld(sessionUID);
    }

    private void badSessionAnswer(int badUID){
        commonAnswer.clear();
        commonAnswer.setErrorText("Пользователь не авторизован или невалидный идентификатор сессии: " +
                badUID);
        logging.setUserName(null);
        logging.log(commonAnswer.getErrorText());
    }
}
