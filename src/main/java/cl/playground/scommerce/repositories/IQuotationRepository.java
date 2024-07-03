package cl.playground.scommerce.repositories;

import cl.playground.scommerce.entities.Quotation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IQuotationRepository {

    List<Quotation> findAllQuotations();

    Optional<Quotation> findQuotationById(Integer id);

    int createQuotation();

    void deleteQuotation(Integer id);
}
