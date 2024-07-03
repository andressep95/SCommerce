package cl.playground.scommerce.repositories;

import cl.playground.scommerce.entities.QuotationItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IQuotationItemRepository {

    void createQuotationItem(Integer quotationId, Integer productId, Integer quantity);

    void deleteByQuotationId(Integer quotationId);

    List<QuotationItem>  findItemsByQuotationId(Integer quotationId);
}
