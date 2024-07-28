package cl.playground.scommerce.repository;

import org.springframework.stereotype.Repository;

import cl.playground.scommerce.entity.QuotationItem;

import java.util.List;

@Repository
public interface IQuotationItemRepository {

    void createQuotationItem(Integer quotationId, Integer productId, Integer quantity);

    void deleteByQuotationId(Integer quotationId);

    List<QuotationItem>  findItemsByQuotationId(Integer quotationId);
}
