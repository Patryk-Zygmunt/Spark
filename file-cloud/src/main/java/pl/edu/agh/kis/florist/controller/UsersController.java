package pl.edu.agh.kis.florist.controller;

import com.google.gson.Gson;
import pl.edu.agh.kis.florist.dao.UserDAO;
import pl.edu.agh.kis.florist.model.Directory;
import pl.edu.agh.kis.florist.model.User;
import spark.Request;
import spark.Response;

/**
 * Created by kantor on 21.01.17.
 */
public class UsersController {
    private UserDAO userDAO;
    private final Gson gson = new Gson();
    private static final int CREATED = 201;

    public UsersController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Object handleCreateDirectory(Request req, Response res){
        String Path=req.params("path");
        String part[]=Path.split("/");

        String userName=part[part.length-1];

        User result = userDAO.createUser(userName);
        res.status(CREATED);
        return result;

    }
}
