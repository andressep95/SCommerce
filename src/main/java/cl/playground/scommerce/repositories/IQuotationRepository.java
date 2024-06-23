package cl.playground.scommerce.repositories;

import cl.playground.scommerce.entities.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface IQuotationRepository extends JpaRepository<Quotation, Integer> {

    @Query(value = "SELECT * FROM quotations", nativeQuery = true)
    List<Quotation> findAllQuotations();

    @Query(value = "SELECT * FROM quotations WHERE id = :id", nativeQuery = true)
    Optional<Quotation> findQuotationById(Integer id);

    @Transactional
    @Query(value = "INSERT INTO quotations DEFAULT VALUES RETURNING id", nativeQuery = true)
    int createQuotation();

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM quotations WHERE id = :id", nativeQuery = true)
    void deleteQuotation(Integer id);
}
