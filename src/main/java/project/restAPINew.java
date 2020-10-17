package project;

import dataClasses.User;
import log.Logging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import services.UserDAOImpl;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RestController
@RequestMapping("/apiNew")
public class restAPINew {
    @Autowired
    private UserDAOImpl userDAO;

    @Autowired
    private Logging logging;

    @GetMapping("/userDepositsNew")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
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
