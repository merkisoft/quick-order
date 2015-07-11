package ch.quickorder.entities;

import java.util.ArrayList;
import java.util.List;

public class ProductGroup {

    private String name;
    private int position;
    private List<Product> products = new ArrayList<>();

    public ProductGroup(String category) {
        if (category.contains( ":")) {
            name = category.split( ":")[ 1];
            position = Integer.parseInt( category.split( ":")[0]);
        } else {
            name = category;
            position = 0;
        }
    }

    public void addProduct( Product product) {
        products.add( product);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
