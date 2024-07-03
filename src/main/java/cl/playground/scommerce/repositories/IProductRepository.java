package cl.playground.scommerce.repositories;

import cl.playground.scommerce.entities.Product;
import java.util.List;
import java.util.Optional;

public interface IProductRepository {
    List<Product> findAllProducts(int page, int size);
    Optional<Product> findProductById(Integer id);
    Optional<Product> findProductByName(String name);
    void createProduct(String name, Double price);
    void updateProduct(Integer id, String name, Double price);
    void deleteProduct(Integer id);
}
