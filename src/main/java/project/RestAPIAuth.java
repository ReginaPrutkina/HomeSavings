package project;

import businessLogicClasses.PercentTypeFactory;
import businessLogicClasses.TypeOfPercent;
import dataClasses.AdminSender;
import dataClasses.Deposit;
import dataClasses.User;
import log.Logging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import services.*;
import services.BDServices.UserDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@Path("/api")
public class RestAPIAuth {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private Logging logging;

    @Autowired
    private PercentTypeFactory percentTypeFactory;

    @Autowired
    private UserService userService;

    @Autowired
    private CommonAnswer commonAnswer;

    @Autowired
    private Security security;

    @Autowired
    private DepositService depositService;

    /**
     * Авторизация клиента.
     *
     * @param login    - логин
     * @param password - пароль
     * @return - При успешной авторизации возвращаем клиента из БД со списком его депозитов
     * или commonAnswer с текстом ошибки
     */
    @GET
    @Path("/authUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity<CommonAnswer> authUser(
            @QueryParam(value = "login") String login,
            @QueryParam(value = "password") String password) {
        logging.setUserName(login);
        logging.log("REST-запрос: Аутентификации клиента. ");
        User user = userService.userAuth(login, password);
        commonAnswer.clear();
        if (user == null) {
            commonAnswer.setErrorText("Клиент не прошел авторизацию");
            return new ResponseEntity<>(commonAnswer, HttpStatus.NOT_FOUND);
        }
        //Генерим и записываем в секьюрити мапу уникальный ид сессии, передаем его в ответе
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.setUser(user);
        commonAnswer.setSessionUID(security.generateAndAddUID(commonRequest));
        commonAnswer.setUser(user);
        return new ResponseEntity<>(commonAnswer, HttpStatus.OK);
    }

    /**
     * Запрос данных клиента по ID клиента. Доступен только админу
     *
     * @param request: обязательные поля: UID сессии, User:ID клиента
     * @return commonAnswer с User-ом из мапы секрьрити или commonAnswer с текстом ошибки
     */
    @POST
    @Path("/userDepositsById")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity<CommonAnswer> getUserDeposits(CommonRequest request) {
        int sessionUID = request.getSessionUID();
        commonAnswer.clear();
        //Проверка наличия сессии в мапе секьюрити
        if (!security.isSessionUIDValid(sessionUID)) {
            badSessionAnswer(sessionUID);
            return new ResponseEntity<>(commonAnswer, HttpStatus.NOT_FOUND);
        }
        logging.setUserName(security.getUIDMap().get(sessionUID).getUser().getLogin());
        logging.log("REST-запрос Поиск данных клиента по ID: " + request.getUser().getId());
        // проверка на админа
        if (!security.getUIDMap().get(sessionUID).getUser().getRole().equals("admin")) {
            commonAnswer.setErrorText("Пользователь не является администратором. " +
                    "Нет прав для получения данных клиента по ID.");
            logging.log(commonAnswer.getErrorText());
            return new ResponseEntity<>(commonAnswer, HttpStatus.FORBIDDEN);
        }
        User user = userDAO.findUserById(request.getUser().getId());
        if (user != null)
            logging.log("Успешный ответ для " + user.toString());
        else
            logging.log("Данные не найдены");
        commonAnswer.setUser(user);
        //обновляем lastUpdate  сессии в мапе
        security.getUIDMap().get(sessionUID).setLastUpdate(new Date());
        return new ResponseEntity<>(commonAnswer, HttpStatus.OK);
    }

    /**
     * Просмотр всех пользователей. Доступно только администратору
     *
     * @param request: обязательные поля: UID сессии
     * @return список всех клиентов и их депоитов из БД или commonAnswer с текстом ошибки
     */
    @POST
    @Path("/showUsers")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity<List<?>> showUsers(CommonRequest request) {
        List<CommonAnswer> commonAnswers = new ArrayList<>();
        int sessionUID = request.getSessionUID();
        //Проверка наличия сессии в мапе секьюрити
        if (!security.isSessionUIDValid(sessionUID)) {
            badSessionAnswer(sessionUID);
            commonAnswers.add(commonAnswer);
            return new ResponseEntity<>(commonAnswers, HttpStatus.NOT_FOUND);
        }
        logging.setUserName(security.getUIDMap().get(sessionUID).getUser().getLogin());
        logging.log("REST-запрос Вывод данных по всем клиентам из БД. ");
        // проверка на админа
        if (!security.getUIDMap().get(sessionUID).getUser().getRole().equals("admin")) {
            commonAnswer.clear();
            commonAnswer.setErrorText("Пользователь не является администратором. " +
                    "Нет прав для получения данных по всем клиентам.");
            logging.log(commonAnswer.getErrorText());
            commonAnswers.add(commonAnswer);
            return new ResponseEntity<>(commonAnswers, HttpStatus.FORBIDDEN);
        }
        List<User> users = userDAO.findAll();
        //обновляем lastUpdate  сессии в мапе
        security.getUIDMap().get(sessionUID).setLastUpdate(new Date());
        logging.log("Успешный ответ. Найдено " + users.size() + " клиентов.");
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * авторизация не требуется. Справочная информация
     *
     * @param type - код типа начисления %%
     * @return строковое название типа начисления %%
     */
    @GET
    @Path("/typeOfPercent")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity<String> getTypeOfPercent(@QueryParam(value = "type") int type ){
        TypeOfPercent typeOfPercent = percentTypeFactory.getTypeOfPercentMap().get(type);
        if (typeOfPercent == null)
            return new ResponseEntity<>("Неверный TypeOfPercent: " + type, HttpStatus.NOT_FOUND);
       return new ResponseEntity<>(typeOfPercent.toString(), HttpStatus.OK);
    }

    /**
     * авторизация не требуется. Справочная информация
     * @param type - код типа начисления %%
     * @param rateOfInterest - заявленная %% ставка
     * @return эффективная %% ставка
     */
    @GET
    @Path("/effectiveRate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity<String> getEffectiveRate(
            @QueryParam(value = "type") int type,
            @QueryParam(value = "rateOfInterest") double rateOfInterest){
        TypeOfPercent typeOfPercent = percentTypeFactory.getTypeOfPercentMap().get(type);
        if (typeOfPercent == null)
            return new ResponseEntity<>("Неверный TypeOfPercent: "+ type,  HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(String.format("%.2f",typeOfPercent.effectiveRate(rateOfInterest)), HttpStatus.OK);
    }

    /**
     * Регистрация клиента: запись клиента в БД и геренация UID сессии
     * @param user - новый клиенент для регистрации в БД
     * @return  - commonAnswer с User-ом и сгенерированным UID сессии
     *            или commonAnswer с текстом ошибки
     */
    @PUT
    @Path("/newUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity<CommonAnswer> registerUser(User user){
        logging.setUserName(user.getLogin());
        logging.log("REST-запрос: Регистрация клиента. ");
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


    /**
     * Регистрация нвого админа. Разрешено только из сессии зарегистрированного админа
     *
     * @param admin - новый админ для регистрации в БД
     * @param sessionUID - UID сессии зарегистрированного админа
     * @return  - commonAnswer с User-ом и сгенерированным UID сессии для нового админа
     *            или commonAnswer с текстом ошибки
     */
    @PUT
    @Path("/newAdmin")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity<CommonAnswer> registerAdmin(AdminSender admin,
                                                      @QueryParam(value = "sessionUID") int sessionUID) {
        commonAnswer.clear();
        //Проверка наличия сессии в мапе секьюрити
        if (!security.isSessionUIDValid(sessionUID)) {
            badSessionAnswer(sessionUID);
            return new ResponseEntity<>(commonAnswer, HttpStatus.NOT_FOUND);
        }
        logging.setUserName(security.getUIDMap().get(sessionUID).getUser().getLogin());
        logging.log("REST-запрос Регистрация нового админа: " + admin.getLogin());
        // проверка на админа
        if (!security.getUIDMap().get(sessionUID).getUser().getRole().equals("admin")) {
            commonAnswer.setErrorText("Пользователь не является администратором. " +
                    "Нет прав для регистрации нового администратора.");
            logging.log(commonAnswer.getErrorText());
            return new ResponseEntity<>(commonAnswer, HttpStatus.FORBIDDEN);
        }
        User newUser = userService.registerUser(admin);
        commonAnswer.clear();
        if (newUser == null){
            commonAnswer.setErrorText("Клиент не прошел валидацию. Регистрация не успешна. ");
            return new ResponseEntity<>(commonAnswer,  HttpStatus.FORBIDDEN);
        }
        //Генерим и записываем в секьюрити мапу уникальный ид сессии, передаем его в ответе
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.setUser(admin);
        commonAnswer.setSessionUID(security.generateAndAddUID(commonRequest));
        commonAnswer.setUser(newUser);
        return new ResponseEntity<>(commonAnswer,  HttpStatus.OK);
    }

    /**
     * Удаление пользователя. Разрешено только для админа
     * @param request: обязательные поля: UID сессии, User:ID клиента
     * @return commonAnswer с пустым user
     */
    @DELETE
    @Path("/deleteUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity<CommonAnswer> deleteUser(CommonRequest request){
        int sessionUID = request.getSessionUID();
        commonAnswer.clear();
        //Проверка наличия сессии в мапе секьюрити
        if (!security.isSessionUIDValid(sessionUID)) {
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

    /**
     * обновляет депозиты по указанным ID. Добавляет депозиты, если ID  не указан
     * проверяем, что обновляемые депозиты  (с  ID) - принадлежат клиенту
     * Возвращаем клиента из БД с обновленным списком депозитов
     * @param request: обязательные поля - UID сессии, User: список депозитов
     * @return commonAnswer c user c  обновленным списком депозитов
     *          или commonAnswer с текстом ошибки
     */
    @POST
    @Path("/updateUserDeposits")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity<CommonAnswer> updateUserDeposits(CommonRequest request) {
        int sessionUID = request.getSessionUID();
        commonAnswer.clear();
        //Проверка наличия сессии в мапе секьюрити
        if (!security.isSessionUIDValid(sessionUID)) {
            badSessionAnswer(sessionUID);
            return new ResponseEntity<>(commonAnswer,  HttpStatus.NOT_FOUND);
        }
        //сохраняем в ответе сессию
        commonAnswer.setSessionUID(sessionUID);
        //Берем клиента из map-ы
        User user = security.getUIDMap().get(sessionUID).getUser();
        logging.setUserName(user.getLogin());
        logging.log("REST-запрос Обновление депозитов клиента : "+ user.getLogin());

        for (Deposit deposit: request.getUser().getDeposits()) {
            deposit.setUser(user);
            //Проверяем, принадлежали ли клиенту депозиты с указанными ID в request
            if (deposit.getId() != 0 && !containsId(deposit.getId(),user.getDeposits())) {
                commonAnswer.setErrorText("Депозит с ID " + deposit.getId() +
                        " не принадлежит клиенту " + user.toString());
                logging.log(commonAnswer.getErrorText());
                return new ResponseEntity<>(commonAnswer, HttpStatus.FORBIDDEN);
            }
            //Проверяем корректность депозитов в списке
            if (!depositService.isDepositValid(deposit)) {
                commonAnswer.setErrorText("Некорретные поля депозита " + deposit.toString());
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

    private void badSessionAnswer(int badUID){
        commonAnswer.clear();
        commonAnswer.setErrorText("Пользователь не авторизован или невалидный идентификатор сессии: " +
                badUID);
        logging.setUserName(null);
        logging.log(commonAnswer.getErrorText());
    }
}
