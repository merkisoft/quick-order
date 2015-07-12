package ch.quickorder.service;

import ch.quickorder.entities.Order;
import ch.quickorder.entities.User;
import ch.quickorder.model.RestaurantsModel;
import ch.quickorder.model.UsersModel;
import ch.quickorder.util.LogPrefix;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Controller
@RequestMapping(produces = "application/json", value = "/users")
public class Users {

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public
    @ResponseBody
    Collection<User> getUsers(@RequestHeader("x-qo-userid") String userId) {
        System.out.println(LogPrefix.currentTime("> Users.getUsers: Begin"));

        try {
            return UsersModel.getInstance().getUsers();
        } finally {
            System.out.println(LogPrefix.currentTime( "< Users.getUsers: End"));
        }
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    User getUserById(@PathVariable String id,
                     @RequestHeader("x-qo-userid") String userId) {
        System.out.println(LogPrefix.currentTime("> Users.getUserById: Begin"));

        try {
            return UsersModel.getInstance().getUserById(id);
        } finally {
            System.out.println(LogPrefix.currentTime( "< Users.getUserById: End"));
        }
    }

    @RequestMapping(value = "/id/{id}/openOrders", method = RequestMethod.GET)
    public
    @ResponseBody
    Collection<Order> getOpenOrdersForUser(@PathVariable String id,
                                           @RequestHeader("x-qo-userid") String userId) {
        System.out.println(LogPrefix.currentTime("> Users.getOpenOrdersForUser: Begin"));

        try {
            return UsersModel.getInstance().getOpenOrdersForUser(id);
        } finally {
            System.out.println(LogPrefix.currentTime( "< Users.getOpenOrdersForUser: End"));
        }
    }
}
