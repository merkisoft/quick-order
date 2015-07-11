package ch.quickorder.service;

import ch.quickorder.entities.Order;
import ch.quickorder.entities.User;
import ch.quickorder.model.OrdersModel;
import ch.quickorder.model.UsersModel;
import ch.quickorder.util.OrderStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @RequestMapping(value = "/id/{id}/openOrders", method = RequestMethod.GET)
    public
    @ResponseBody
    Collection<Order> getOpenOrdersForUser(@PathVariable String id,
                                           @RequestHeader("x-qo-userid") String userId) {
        User user = UsersModel.getInstance().getUserById(id);

        if (user == null) {
            return null;
        }

        Collection< Order> orderList = new ArrayList<>();

        for( String orderId : user.getOrders()) {
            Order order = OrdersModel.getInstance().getOrderById(orderId);

            if (order == null) {
                continue;
            }

            if (order.getStatus().equals( OrderStatus.Ordered.name())
                    || order.getStatus().equals( OrderStatus.Paid)) {
                orderList.add( order);
            }
        }

        return orderList;
    }
}
