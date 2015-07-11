package ch.quickorder.service;

import ch.quickorder.entities.User;
import ch.quickorder.model.UsersModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Controller
@RequestMapping(produces = "application/json", value = "/rest/users")
public class Users {

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public
    @ResponseBody
    Collection<User> getUsers(@RequestHeader("x-qo-userid") String userId) {
        return UsersModel.getInstance().getUsers();
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    User getUserById(@PathVariable String id,
                     @RequestHeader("x-qo-userid") String userId) {
        return UsersModel.getInstance().getUserById(id);
    }
}
