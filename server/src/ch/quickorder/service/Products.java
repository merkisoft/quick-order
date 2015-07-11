package ch.quickorder.service;

import ch.quickorder.entities.Product;
import ch.quickorder.model.ProductsModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping(produces = "application/json")
public class Products {

    @RequestMapping(value = "/products/all", method = RequestMethod.GET)
    public
    @ResponseBody
    Collection<Product> getProducts() {
        return ProductsModel.getInstance().getProducts();
    }

    @RequestMapping(value = "/product/id/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Product getProduct(@PathVariable String id) {
        return ProductsModel.getInstance().getProductById(id);
    }

    @RequestMapping(value = "/product/{restaurant}/all", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Product> getProductsForRestaurant(@PathVariable String restaurant) {
        return ProductsModel.getInstance().getProductsForRestaurant(restaurant);
    }


}
