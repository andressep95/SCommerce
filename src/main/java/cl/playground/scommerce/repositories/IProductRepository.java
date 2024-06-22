package cl.playground.scommerce.repositories;

import cl.playground.scommerce.entities.Product;
import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product, Integer> {

    @Query(value = "SELECT * FROM products", nativeQuery = true)
    List<Product> findAllProducts();

    @Query(value = "SELECT * FROM products WHERE id = :id", nativeQuery = true)
    Product findProductById(Integer id);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO products (name, price) VALUES (:name, :price)", nativeQuery = true)
    void createProduct(String name, Double price);

    @Transactional
    @Modifying
    @Query(value = "UPDATE products SET name = :name, price = :price WHERE id = :id", nativeQuery = true)
    void updateProduct(Integer id, String name, Double price);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM products WHERE id = :id", nativeQuery = true)
    void deleteProduct(Integer id);
}
