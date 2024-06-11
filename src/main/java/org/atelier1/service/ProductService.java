package org.atelier1.service;

import org.atelier1.exception.CannotDeleteProductException;
import org.atelier1.exception.ProductAlreadyExistsException;
import org.atelier1.exception.ProductNotFoundException;
import org.atelier1.model.Product;

import java.util.*;
import java.util.stream.Collectors;

public class ProductService {
    public Map<Long, Product> productDatabase = new HashMap<>();
    private Long idCounter = 1L;

    public Product addProduct(String name, String category, double price, int quantityInStock) {
        if (productDatabase.values().stream().anyMatch(prod -> prod.getName().equals(name) && prod.getCategory().equals(category))) {
            throw new ProductAlreadyExistsException("Product with name " + name + " already exists in category " + category + ".");
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
        if (product.getQuantityInStock() > 0) {
            throw new CannotDeleteProductException("Cannot delete product that is in stock.");
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
        product.setQuantity(quantityInStock);
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

    public void updateStock(Long id, int quantityInStock) {
        Product product = productDatabase.get(id);
        if (product == null) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        product.setQuantity(quantityInStock);
    }

    public List<Product> getProductsBelowThreshold(int threshold) {
        return productDatabase.values().stream()
                .filter(prod -> prod.getQuantityInStock() < threshold)
                .collect(Collectors.toList());
    }
}
