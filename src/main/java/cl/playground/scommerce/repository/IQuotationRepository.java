package cl.playground.scommerce.repository;

import org.springframework.stereotype.Repository;

import cl.playground.scommerce.entity.Quotation;

import java.util.List;
import java.util.Optional;

@Repository
public interface IQuotationRepository {

    List<Quotation> findAllQuotations(int page, int size);

    Optional<Quotation> findQuotationById(Integer id);

    int createQuotation();

    void deleteQuotation(Integer id);
}
