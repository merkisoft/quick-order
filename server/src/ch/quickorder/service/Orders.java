package ch.quickorder.service;

import ch.quickorder.entities.Order;
import ch.quickorder.model.OrdersModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;

@Controller
@RequestMapping(produces = "application/json", value = "/rest/orders")
public class Orders {
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public
    @ResponseBody
    Collection<Order> getOrders() {
        return OrdersModel.getInstance().getOrders();
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Order getOrderById(@PathVariable String id) {
        return OrdersModel.getInstance().getUserById(id);
    }


}
