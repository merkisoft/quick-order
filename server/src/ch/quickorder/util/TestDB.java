package ch.quickorder.util;

import ch.quickorder.entities.Order;
import ch.quickorder.model.OrdersModel;
import ch.quickorder.model.ProductsModel;
import ch.quickorder.model.RestaurantsModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestDB {


    public static void main(String[] args) throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 100; i++) {
            es.submit(() ->
                    System.out.println(OrdersModel.getInstance().getOrders().size()));
            es.submit(() -> System.out.println(ProductsModel.getInstance().getProducts().size()));
            es.submit(() -> System.out.println(RestaurantsModel.getInstance().getRestaurantById("1").getId()));
            es.submit(() -> {
                Order order = new Order();
                order.setRestaurant("1");
                System.out.println(OrdersModel.getInstance().createOrder("2", order).getId());
            });
        }

        es.shutdown();
        es.awaitTermination(1, TimeUnit.HOURS);

    }
}
