package cl.playground.scommerce.repositories;

import cl.playground.scommerce.entities.Product;
import cl.playground.scommerce.exceptions.DatabaseException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NativeProductRepository implements IProductRepository {
    private final DataSource dataSource;

    public NativeProductRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Product> findAllProducts(int page, int size) {
        String sql = "SELECT * FROM products LIMIT ? OFFSET ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, size);
            ps.setInt(2, size * (page - 1));
            ResultSet rs = ps.executeQuery();

            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                products.add(mapRowToProduct(rs));
            }
            return products;
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving all products");
        }
    }

    public Optional<Product> findProductById(Integer id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToProduct(rs));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving product by ID");
        }
    }

    public Optional<Product> findProductByName(String name) {
        String sql = "SELECT * FROM products WHERE name = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToProduct(rs));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving product by name");
        }
    }

    public void createProduct(String name, Double price) {
        String sql = "INSERT INTO products (name, price) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error creating product");
        }
    }

    public void updateProduct(Integer id, String name, Double price) {
        String sql = "UPDATE products SET name = ?, price = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setInt(3, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating product");
        }
    }

    public void deleteProduct(Integer id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting product");
        }
    }

    private Product mapRowToProduct(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getDouble("price")
        );
    }
}
