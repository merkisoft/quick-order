package ch.quickorder.service;

import ch.quickorder.entities.*;
import ch.quickorder.model.OrdersModel;
import ch.quickorder.model.ProductDetailsModel;
import ch.quickorder.model.ProductsModel;
import ch.quickorder.model.RestaurantsModel;
import ch.quickorder.util.LogPrefix;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Controller
@RequestMapping(produces = "application/json", value = "/restaurants")
public class Restaurants {

    @RequestMapping(value = "/{restaurant}/products/all", method = RequestMethod.GET)
    public
    @ResponseBody
    Collection<Product> getProductsForRestaurant(@PathVariable String restaurant) {
        System.out.println(LogPrefix.currentTime("> Restaurants.getProductsForRestaurant: Begin"));

        try {
            return ProductsModel.getInstance().getProductsForRestaurant(restaurant);
        } finally {
            System.out.println(LogPrefix.currentTime( "< Restaurants.getProductsForRestaurant: End"));
        }
    }

    @RequestMapping(value = "/{restaurant}/products/groups", method = RequestMethod.GET)
    public
    @ResponseBody
    Collection< ProductGroup> getProductGroupsForRestaurant(@RequestParam(value="locale", defaultValue="en") String locale,
                                                            @PathVariable String restaurant) {
        System.out.println(LogPrefix.currentTime("> Restaurants.getProductGroupsForRestaurant: Begin"));

        try {
            return ProductsModel.getInstance().getProductGroupsForRestaurant(restaurant);
        } finally {
            System.out.println(LogPrefix.currentTime( "< Restaurants.getProductGroupsForRestaurant: End"));
        }
    }

    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    public
    @ResponseBody
    Collection< Restaurant> getRestaurantsByName(@PathVariable String name) {
        System.out.println(LogPrefix.currentTime("> Restaurants.getRestaurantsByName: Begin"));

        try {
            return RestaurantsModel.getInstance().getRestaurantsByName(name);
        } finally {
            System.out.println(LogPrefix.currentTime( "< Restaurants.getRestaurantsByName: End"));
        }
    }

    @RequestMapping(value = "/city/{city}", method = RequestMethod.GET)
    public
    @ResponseBody
    Collection< Restaurant> getRestaurantsByCity(@PathVariable String city) {
        System.out.println(LogPrefix.currentTime("> Restaurants.getRestaurantsByCity: Begin"));

        try {
            return RestaurantsModel.getInstance().getRestaurantsByCity(city);
        } finally {
            System.out.println(LogPrefix.currentTime( "< Restaurants.getRestaurantsByCity: End"));
        }
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Restaurant getRestaurantById(@PathVariable String id) {
        System.out.println(LogPrefix.currentTime("> Restaurants.getRestaurantById: Begin"));

        try {
            return RestaurantsModel.getInstance().getRestaurantById(id);
        } finally {
            System.out.println(LogPrefix.currentTime( "< Restaurants.getRestaurantById: End"));
        }
    }
}
