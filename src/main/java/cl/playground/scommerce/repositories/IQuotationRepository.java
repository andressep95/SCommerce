package cl.playground.scommerce.repositories;

import cl.playground.scommerce.entities.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IQuotationRepository extends JpaRepository<Quotation, Integer> {
}
