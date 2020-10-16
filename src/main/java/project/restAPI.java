package project;

import dataClasses.User;
import log.Logging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import services.UserDAOImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Service
@Path("/api")
public class restAPI {

    @Autowired
    private UserDAOImpl userDAO;

    @Autowired
    private Logging logging;

    @GET
    @Path("/userDeposits")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public User getUserDeposits(){
        if (userDAO==null)
            userDAO = new UserDAOImpl();
        User user = userDAO.findUserByLogin("petrov");
        return user;
//        public Data getDataByNames(@RequestParam(value = "name") List<String> names) {
//            return userService.getDataByNames(names);
//        }
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
