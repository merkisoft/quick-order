package ch.quickorder.service;

import ch.quickorder.entities.Product;
import ch.quickorder.model.ProductsModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Controller
@RequestMapping(produces = "application/json", value = "/rest/products")
public class Products {

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public
    @ResponseBody
    Collection<Product> getProducts() {
        return ProductsModel.getInstance().getProducts();
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Product getProductById(@PathVariable String id) {
        return ProductsModel.getInstance().getProductById(id);
    }

    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    public
    @ResponseBody
    Collection<Product> getProductByName(@PathVariable String name) {
        return ProductsModel.getInstance().getProductsByName(name);
    }

}
