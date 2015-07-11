package ch.quickorder.model;

public class OrderModel extends CpsBasedModel {
    private static OrderModel ourInstance = new OrderModel();

    public static OrderModel getInstance() {
        return ourInstance;
    }

    private OrderModel() {
        super("Orders");
    }
}
