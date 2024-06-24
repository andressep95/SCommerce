package cl.playground.scommerce.repositories;

import cl.playground.scommerce.entities.QuotationItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface IQuotationItemRepository extends JpaRepository<QuotationItem, Integer> {

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO quotation_items (quotation_id, product_id, quantity) VALUES (:quotationId, :productId, :quantity)", nativeQuery = true)
    void createQuotationItem(Integer quotationId, Integer productId, Integer quantity);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM quotation_items WHERE quotation_id = :quotationId", nativeQuery = true)
    void deleteByQuotationId(Integer quotationId);

}
