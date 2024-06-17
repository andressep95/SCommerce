package cl.playground.scommerce.repositories;

import cl.playground.scommerce.entities.QuotationItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IQuotationItemRepository extends JpaRepository<QuotationItem, Integer> {
    void deleteByQuotationId(int quotationId);
}
