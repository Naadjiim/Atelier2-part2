package org.atelier1.model;

public class Product {
    private Long id;
    private String name;
    private String category;
    private double price;
    private int quantityInStock;

    public Product(Long id, String name, String category, double price, int quantityInStock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantityInStock = quantityInStock;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantity(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }
}
