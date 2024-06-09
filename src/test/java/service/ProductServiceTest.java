package service;

import org.atelier1.exception.CannotDeleteProductException;
import org.atelier1.exception.ProductAlreadyExistsException;
import org.atelier1.exception.ProductNotFoundException;
import  org.atelier1.model.Product;
import org.atelier1.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        productService = new ProductService();
    }

    @Test
    public void testAddProduct() {
        Product product = productService.addProduct("Laptop", "Electronics", 1000.0, 50);
        assertNotNull(product.getId());
        assertEquals("Laptop", product.getName());
    }

    @Test
    public void testAddProductDuplicateName() {
        productService.addProduct("Laptop", "Electronics", 1000.0, 50);
        assertThrows(ProductAlreadyExistsException.class, () ->
                productService.addProduct("Laptop", "Electronics", 1500.0, 30));
    }

    @Test
    public void testDeleteProduct() {
        Product product = productService.addProduct("Laptop", "Electronics", 1000.0, 50);
        productService.deleteProduct(product.getId());
        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(product.getId()));
    }

    @Test
    public void testDeleteProductInOrder() {
        Product product = productService.addProduct("Laptop", "Electronics", 1000.0, 50);
        productService.addProductToOrder(product.getId());
        assertThrows(CannotDeleteProductException.class, () -> productService.deleteProduct(product.getId()));
    }

    @Test
    public void testUpdateProduct() {
        Product product = productService.addProduct("Laptop", "Electronics", 1000.0, 50);
        productService.updateProduct(product.getId(), "Laptop Pro", "Electronics", 1200.0, 60);
        Product updatedProduct = productService.updateProduct(product.getId(), "Laptop Pro", "Electronics", 1200.0, 60);
        assertEquals("Laptop Pro", updatedProduct.getName());
    }

    @Test
    public void testListAllProducts() {
        productService.addProduct("Laptop", "Electronics", 1000.0, 50);
        productService.addProduct("Shampoo", "Personal Care", 10.0, 200);
        List<Product> products = productService.listAllProducts();
        assertEquals(2, products.size());
    }

    @Test
    public void testSearchProducts() {
        productService.addProduct("Laptop", "Electronics", 1000.0, 50);
        productService.addProduct("Shampoo", "Personal Care", 10.0, 200);
        List<Product> products = productService.searchProducts("Shampoo", null, null, 1, 10);
        assertEquals(1, products.size());
        assertEquals("Shampoo", products.get(0).getName());
    }

    @Test
    public void testUpdateStock() {
        Product product = productService.addProduct("Laptop", "Electronics", 1000.0, 50);
        productService.updateStock(product.getId(), 5);
        assertEquals(5, product.getQuantityInStock());
    }
}
