package ch.quickorder.entities;

import ch.quickorder.entities.Product;

import java.util.List;

public class ProductGroup {

    private String name;
    private List<Product> products;

    public ProductGroup(String name) {
        this.name = name;
    }

    public void addProduct( Product product) {
        products.add( product);
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
