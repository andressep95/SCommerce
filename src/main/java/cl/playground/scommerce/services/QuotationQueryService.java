package cl.playground.scommerce.services;

import cl.playground.scommerce.dtos.QuotationDTO;
import cl.playground.scommerce.entities.Quotation;
import cl.playground.scommerce.exceptions.QuotationExceptionNotFound;
import cl.playground.scommerce.mappers.QuotationMapper;
import cl.playground.scommerce.repositories.IQuotationRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuotationQueryService {

    private final IQuotationRepository quotationRepository;
    private final QuotationMapper quotationMapper;

    public QuotationQueryService(IQuotationRepository quotationRepository, QuotationMapper quotationMapper) {
        this.quotationRepository = quotationRepository;
        this.quotationMapper = quotationMapper;
    }

    @Cacheable(value = "quotations")
    public List<QuotationDTO> getAllQuotations() {
        return quotationRepository.findAllQuotations().stream()
                .map(quotationMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "quotations", key = "#id")
    public QuotationDTO getQuotationById(Integer id) {
        Optional<Quotation> quotation = quotationRepository.findQuotationById(id);
        return quotation.map(quotationMapper::mapToDTO)
                .orElseThrow(() -> new QuotationExceptionNotFound("Quotation not found"));
    }
}
