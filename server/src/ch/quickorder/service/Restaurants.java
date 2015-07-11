package ch.quickorder.service;

import ch.quickorder.entities.Product;
import ch.quickorder.entities.ProductGroup;
import ch.quickorder.entities.Restaurant;
import ch.quickorder.model.ProductsModel;
import ch.quickorder.model.RestaurantsModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Controller
@RequestMapping(produces = "application/json", value = "/rest/restaurants")
public class Restaurants {

    @RequestMapping(value = "/{restaurant}/products/all", method = RequestMethod.GET)
    public
    @ResponseBody
    Collection<Product> getProductsForRestaurant(@PathVariable String restaurant) {
        return ProductsModel.getInstance().getProductsForRestaurant(restaurant);
    }

    @RequestMapping(value = "/{restaurant}/products/groups", method = RequestMethod.GET)
    public
    @ResponseBody
    Collection< ProductGroup> getProductGroupsForRestaurant(@RequestParam(value="locale", defaultValue="en") String locale,
                                                            @PathVariable String restaurant) {
        return ProductsModel.getInstance().getProductGroupsForRestaurant(restaurant);
    }

    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    public
    @ResponseBody
    Collection< Restaurant> getRestaurantsByName(@PathVariable String name) {
        return RestaurantsModel.getInstance().getRestaurantsByName( name);
    }

    @RequestMapping(value = "/city/{city}", method = RequestMethod.GET)
    public
    @ResponseBody
    Collection< Restaurant> getRestaurantsByCity(@PathVariable String city) {
        return RestaurantsModel.getInstance().getRestaurantsByCity( city);
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Restaurant getRestaurantById(@PathVariable String id) {
        return RestaurantsModel.getInstance().getRestaurantById( id);
    }

}
