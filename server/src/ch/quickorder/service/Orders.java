package ch.quickorder.service;

import ch.quickorder.entities.Order;
import ch.quickorder.model.OrdersModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Controller
@RequestMapping(produces = "application/json", value = "/rest/orders")
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

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public
    @ResponseBody
    Order createOrder(@PathVariable Order order,
                      @RequestHeader("x-qo-userid") String user) {
        return OrdersModel.getInstance().createOrder(user, order);
    }

    @RequestMapping(value = "/id/{id}/markAsPaid", method = RequestMethod.PUT)
    public
    @ResponseBody
    boolean markOrderAsPaid(@PathVariable String id,
                          @RequestHeader("x-qo-userid") String user) {
        return OrdersModel.getInstance().markOrderAsPaid(user, id);
    }


}
