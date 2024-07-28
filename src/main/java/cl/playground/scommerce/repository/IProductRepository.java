package cl.playground.scommerce.repository;

import java.util.List;
import java.util.Optional;

import cl.playground.scommerce.entity.Product;

public interface IProductRepository {
    List<Product> getProductsForReport();
    List<Product> findAllProducts(int page, int size);
    Optional<Product> findProductById(Integer id);
    Optional<Product> findProductByName(String name);
    void createProduct(String name, Double price);
    void updateProduct(Integer id, String name, Double price);
    void deleteProduct(Integer id);
}
