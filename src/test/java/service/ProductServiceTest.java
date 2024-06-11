package service;

import org.atelier1.exception.CannotDeleteProductException;
import org.atelier1.exception.ProductAlreadyExistsException;
import org.atelier1.exception.ProductNotFoundException;
import org.atelier1.model.Product;
import org.atelier1.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private Map<Long, Product> mockProductDatabase;

    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService();
        productService.productDatabase = mockProductDatabase;
    }

    @Test
    public void testAddProduct() {
        Product product = new Product(1L, "iPhone", "Electronics", 999.99, 10);
        when(mockProductDatabase.values()).thenReturn(Collections.emptyList());

        Product result = productService.addProduct("iPhone", "Electronics", 999.99, 10);

        assertEquals(product, result);
        verify(mockProductDatabase, times(1)).put(any(Long.class), any(Product.class));
    }

    @Test
    public void testAddProductAlreadyExists() {
        Product existingProduct = new Product(1L, "iPhone", "Electronics", 999.99, 10);
        when(mockProductDatabase.values()).thenReturn(Collections.singletonList(existingProduct));

        assertThrows(ProductAlreadyExistsException.class, () ->
                productService.addProduct("iPhone", "Electronics", 999.99, 10));
        verify(mockProductDatabase, never()).put(any(Long.class), any(Product.class));
    }

    @Test
    public void testDeleteProduct() {
        Product product = new Product(1L, "iPhone", "Electronics", 999.99, 0);
        when(mockProductDatabase.get(1L)).thenReturn(product);

        productService.deleteProduct(1L);

        verify(mockProductDatabase, times(1)).remove(1L);
    }

    @Test
    public void testDeleteProductWithStock() {
        Product product = new Product(1L, "iPhone", "Electronics", 999.99, 5);
        when(mockProductDatabase.get(1L)).thenReturn(product);

        assertThrows(CannotDeleteProductException.class, () -> productService.deleteProduct(1L));
        verify(mockProductDatabase, never()).remove(1L);
    }

    @Test
    public void testDeleteProductNotFound() {
        when(mockProductDatabase.get(1L)).thenReturn(null);

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(1L));
        verify(mockProductDatabase, never()).remove(1L);
    }

    @Test
    public void testUpdateProduct() {
        Product product = new Product(1L, "iPhone", "Electronics", 999.99, 10);
        when(mockProductDatabase.get(1L)).thenReturn(product);

        Product updatedProduct = productService.updateProduct(1L, "iPhone X", "Electronics", 1099.99, 20);

        assertEquals("iPhone X", updatedProduct.getName());
        assertEquals(1099.99, updatedProduct.getPrice());
        assertEquals(20, updatedProduct.getQuantityInStock());
        verify(mockProductDatabase, times(1)).put(eq(1L), any(Product.class));
    }

    @Test
    public void testUpdateProductNotFound() {
        when(mockProductDatabase.get(1L)).thenReturn(null);

        assertThrows(ProductNotFoundException.class, () ->
                productService.updateProduct(1L, "iPhone X", "Electronics", 1099.99, 20));
        verify(mockProductDatabase, never()).put(any(Long.class), any(Product.class));
    }
}
