package cl.playground.scommerce.repository;

import cl.playground.scommerce.entity.Quotation;
import cl.playground.scommerce.exception.DatabaseException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuotationRepository implements IQuotationRepository {
    private final DataSource dataSource;
    private final QuotationItemRepository nativeQuotationItemRepository;

    public QuotationRepository(DataSource dataSource, QuotationItemRepository nativeQuotationItemRepository) {
        this.dataSource = dataSource;
        this.nativeQuotationItemRepository = nativeQuotationItemRepository;
    }

    @Override
    public List<Quotation> findAllQuotations(int page, int size) {
        String sql = "SELECT * FROM quotations LIMIT ? OFFSET ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, size);
            ps.setInt(2, size * (page - 1));

            try (ResultSet rs = ps.executeQuery()) {
                List<Quotation> quotations = new ArrayList<>();
                while (rs.next()) {
                    Quotation quotation = mapRowToQuotation(rs);
                    quotation.setItems(nativeQuotationItemRepository.findItemsByQuotationId(quotation.getId()));
                    quotations.add(quotation);
                }
                return quotations;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving all quotations");
        }
    }

    @Override
    public Optional<Quotation> findQuotationById(Integer id) {
        String sql = "SELECT * FROM quotations WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Quotation quotation = mapRowToQuotation(rs);
                    quotation.setItems(nativeQuotationItemRepository.findItemsByQuotationId(quotation.getId()));
                    return Optional.of(quotation);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving quotation by ID");
        }
    }

    @Override
    public int createQuotation() {
        String sql = "INSERT INTO quotations DEFAULT VALUES RETURNING id";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new DatabaseException("Failed to create quotation");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error creating quotation");
        }
    }

    @Override
    public void deleteQuotation(Integer id) {
        String sql = "DELETE FROM quotations WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting quotation");
        }
    }

    private Quotation mapRowToQuotation(ResultSet rs) throws SQLException {
        Quotation quotation = new Quotation();
        quotation.setId(rs.getInt("id"));
        quotation.setCreatedAt(rs.getTimestamp("created_at"));
        quotation.setTotal(rs.getDouble("total"));
        return quotation;
    }
}
