package org.atelier1.service;

import org.atelier1.exception.CannotDeleteProductException;
import org.atelier1.exception.ProductAlreadyExistsException;
import org.atelier1.exception.ProductNotFoundException;
import org.atelier1.model.Product;

import java.util.*;
import java.util.stream.Collectors;

public class ProductService {
    private Map<Long, Product> productDatabase = new HashMap<>();
    private Long idCounter = 1L;
    private Set<Long> productsInOrder = new HashSet<>();

    public Product addProduct(String name, String category, double price, int quantityInStock) {
        if (productDatabase.values().stream().anyMatch(prod -> prod.getName().equals(name) && prod.getCategory().equals(category))) {
            throw new ProductAlreadyExistsException("Product already exists in category: " + category);
        }
        Product product = new Product(idCounter++, name, category, price, quantityInStock);
        productDatabase.put(product.getId(), product);
        return product;
    }

    public void deleteProduct(Long id) {
        Product product = productDatabase.get(id);
        if (product == null) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        if (productsInOrder.contains(id)) {
            throw new CannotDeleteProductException("Cannot delete product that is in order.");
        }
        productDatabase.remove(id);
    }

    public Product updateProduct(Long id, String name, String category, double price, int quantityInStock) {
        Product product = productDatabase.get(id);
        if (product == null) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        product.setName(name);
        product.setCategory(category);
        product.setPrice(price);
        product.setQuantityInStock(quantityInStock);
        return product;
    }

    public List<Product> listAllProducts() {
        return productDatabase.values().stream()
                .sorted(Comparator.comparing(Product::getCategory))
                .collect(Collectors.toList());
    }

    public List<Product> searchProducts(String name, String category, Double price, int page, int size) {
        return productDatabase.values().stream()
                .filter(prod -> (name == null || prod.getName().equalsIgnoreCase(name)) &&
                        (category == null || prod.getCategory().equalsIgnoreCase(category)) &&
                        (price == null || prod.getPrice() == price))
                .skip((page - 1) * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    public void updateStock(Long id, int quantity) {
        Product product = productDatabase.get(id);
        if (product == null) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        product.setQuantityInStock(quantity);
        if (quantity < 10) {
            System.out.println("Alert: Product " + product.getName() + " in category " + product.getCategory() + " is below critical stock level.");
        }
    }

    public void addProductToOrder(Long id) {
        if (productDatabase.containsKey(id)) {
            productsInOrder.add(id);
        } else {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
    }

    public void removeProductFromOrder(Long id) {
        productsInOrder.remove(id);
    }
}
