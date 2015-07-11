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
