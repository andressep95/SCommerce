package cl.playground.scommerce.repositories;

import cl.playground.scommerce.entities.QuotationItem;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IQuotationItemRepository extends JpaRepository<QuotationItem, Integer> {

    @Query(value = "SELECT * FROM quotation_items", nativeQuery = true)
    List<QuotationItem> findAllQuotationItems();

    @Query(value = "SELECT * FROM quotation_items WHERE id = :id", nativeQuery = true)
    QuotationItem findQuotationItemById(Integer id);

    @Query(value = "SELECT * FROM quotation_items WHERE quotation_id = :quotationId", nativeQuery = true)
    List<QuotationItem> findQuotationItemsByQuotationId(Integer quotationId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO quotation_items (quotation_id, product_id, quantity) VALUES (:quotationId, :productId, :quantity)", nativeQuery = true)
    void createQuotationItem(Integer quotationId, Integer productId, Integer quantity);

    @Transactional
    @Modifying
    @Query(value = "UPDATE quotation_items SET quotation_id = :quotationId, product_id = :productId, quantity = :quantity WHERE id = :id", nativeQuery = true)
    void updateQuotationItem(Integer id, Integer quotationId, Integer productId, Integer quantity);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM quotation_items WHERE id = :id", nativeQuery = true)
    void deleteQuotationItem(Integer id);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM quotation_items WHERE quotation_id = :quotationId", nativeQuery = true)
    void deleteByQuotationId(Integer quotationId);

}
