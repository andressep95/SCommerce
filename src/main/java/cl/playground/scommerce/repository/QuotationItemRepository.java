package cl.playground.scommerce.repository;

import cl.playground.scommerce.entity.Product;
import cl.playground.scommerce.entity.Quotation;
import cl.playground.scommerce.entity.QuotationItem;
import cl.playground.scommerce.exception.DatabaseException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuotationItemRepository implements IQuotationItemRepository {
    private final DataSource dataSource;

    public QuotationItemRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void createQuotationItem(Integer quotationId, Integer productId, Integer quantity) {
        String sql = "INSERT INTO quotation_items (quotation_id, product_id, quantity) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, quotationId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error creating quotation item");
        }
    }

    @Override
    public void deleteByQuotationId(Integer quotationId) {
        String sql = "DELETE FROM quotation_items WHERE quotation_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, quotationId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting quotation items by quotation ID");
        }
    }

    public List<QuotationItem> findItemsByQuotationId(Integer quotationId) {
        String sql = "SELECT * FROM quotation_items WHERE quotation_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, quotationId);
            try (ResultSet rs = ps.executeQuery()) {
                List<QuotationItem> items = new ArrayList<>();
                while (rs.next()) {
                    items.add(mapRowToQuotationItem(rs));
                }
                return items;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving quotation items by quotation ID");
        }
    }

    private QuotationItem mapRowToQuotationItem(ResultSet rs) throws SQLException {
        QuotationItem item = new QuotationItem();
        item.setId(rs.getInt("id"));

        Quotation quotation = new Quotation();
        quotation.setId(rs.getInt("quotation_id"));
        item.setQuotation(quotation);

        int productId = rs.getInt("product_id");
        item.setProduct(getProductDetails(productId));

        item.setQuantity(rs.getInt("quantity"));
        return item;
    }

    private Product getProductDetails(int productId) throws SQLException {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.setPrice(rs.getDouble("price"));
                    return product;
                } else {
                    throw new SQLException("Product not found with id: " + productId);
                }
            }
        }
    }
}
