package project;

import businessLogicClasses.PercentTypeFactory;
import businessLogicClasses.TypeOfPercent;
import dataClasses.Deposit;
import dataClasses.User;
import log.Logging;
import org.springframework.beans.factory.annotation.Qualifier;
import services.UserDAOImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import services.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Service
@Path("/api")
public class RestAPI {

    @Autowired
    private UserDAOImpl userDAO;

    @Autowired
    private Logging logging;

    @Autowired
    PercentTypeFactory percentTypeFactory;

    @Autowired
    @Qualifier ("UserService")
    UserService userService;

    @GET
    @Path("/userDepositsByLogin")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public User getUserDeposits(@QueryParam(value = "login") String login){
        logging.log("REST-запрос @QueryParam(value = " + login + ")");
        User user = userDAO.findUserByLogin(login);
        if (user != null)
            logging.log("Успешный ответ для " + user.toString());
        else
            logging.log("Данные не найдены");
        return user;
    }

    @GET
    @Path("/userDepositsById")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public User getUserDeposits(@QueryParam(value = "id") int id){
        logging.log("REST-запрос @QueryParam(value = " + id + ")");
        User user = userDAO.findUserById(id);
        if (user != null)
            logging.log("Успешный ответ для " + user.toString());
        else
            logging.log("Данные не найдены");
        return user;
    }

    @GET
    @Path("/allUsers")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<User> getAllUsers(){
        logging.log("REST-запрос ");
        List<User> users = userDAO.findAll();
        if (users.size() > 0)
            logging.log("Успешный ответ" );
        else
            logging.log("Данные не найдены");
        return users;
    }

    @GET
    @Path("/showUsers")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity<List<User>> showUsers(){
        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", "application/json");
        List<User> users = userDAO.findAll();
        if (users.size() == 0)
            // class error private texterror
            return new ResponseEntity<>(null, header, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(users, header, HttpStatus.OK);
    }

    @GET
    @Path("/typeOfPercent")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity<String> getTypeOfPercent(@QueryParam(value = "type") int type){
        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", "application/json");
        TypeOfPercent typeOfPercent = percentTypeFactory.getTypeOfPercentMap().get(type);
        if (typeOfPercent == null)
            return new ResponseEntity<>(null, header, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(typeOfPercent.toString(), header, HttpStatus.OK);
    }

    @PUT
    @Path("/newUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public User registerUser(User user){
        User newUser = userService.registerUser(user);
        return newUser;
    }

    @DELETE
    @Path("/deleteUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean deleteUser(@QueryParam(value = "id") int id){
        Boolean result;
        User user = userDAO.findUserById(id);
        if (user == null)
            return false;
        userDAO.delete(user);
        return true;
    }

    @POST
    @Path("/updateUserDeposits")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean updateUserDeposits(@QueryParam(value = "id") int id,
                                      List<Deposit> deposits){
        Boolean result;
        User user = userDAO.findUserById(id);
        if (user == null)
            return false;
        for (Deposit deposit: deposits ) {
            deposit.setUser(user);
        }
        user.setDeposits(deposits);
        userDAO.merge(user);
        return true;
    }

    public Logging getLogging() {
        return logging;
    }

    public void setLogging(Logging logging) {
        this.logging = logging;
    }

    public UserDAOImpl getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAOImpl userDAO) {
        this.userDAO = userDAO;
    }



}
