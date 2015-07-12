package ch.quickorder.service;

import ch.quickorder.entities.Order;
import ch.quickorder.model.OrdersModel;
import ch.quickorder.util.LogPrefix;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Controller
@RequestMapping(produces = "application/json", value = "/orders")
public class Orders {
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public
    @ResponseBody
    Collection<Order> getOrders(@RequestHeader("x-qo-userid") String userId) {
        return OrdersModel.getInstance().getOrders();
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Order getOrderById(@PathVariable String id,
                       @RequestHeader("x-qo-userid") String userId) {
        return OrdersModel.getInstance().getOrderById(id);
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    boolean deleteOrderById(@PathVariable String id,
                            @RequestHeader("x-qo-userid") String userId) {
        return OrdersModel.getInstance().deleteOrderById(id, userId);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public
    @ResponseBody
    Order createOrder(@RequestBody Order order,
                      @RequestHeader("x-qo-userid") String user) {
        System.out.println(LogPrefix.currentTime( "> Orders.createOrder: Begin"));

        try {
            return OrdersModel.getInstance().createOrder(user, order);
        } finally {
            System.out.println(LogPrefix.currentTime( "< Orders.createOrder: End"));
        }
    }

    @RequestMapping(value = "/markAsPaid/{id}", method = RequestMethod.PUT)
    public
    @ResponseBody
    boolean markOrderAsPaid(@PathVariable String id,
                          @RequestHeader("x-qo-userid") String user) {
        System.out.println(LogPrefix.currentTime( "> Orders.markOrderAsPaid: Begin"));

        try {
            return OrdersModel.getInstance().markOrderAsPaid(user, id);
        } finally {
            System.out.println(LogPrefix.currentTime( "< Orders.markOrderAsPaid: End"));
        }
    }
}
