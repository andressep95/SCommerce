package cl.playground.scommerce.service.query;

import cl.playground.scommerce.dtos.QuotationDTO;
import cl.playground.scommerce.entity.Quotation;
import cl.playground.scommerce.exception.QuotationExceptionNotFound;
import cl.playground.scommerce.mapper.QuotationMapper;
import cl.playground.scommerce.repository.IQuotationRepository;

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
    public List<QuotationDTO> getAllQuotations(int page, int size) {
        return quotationRepository.findAllQuotations(page, size).stream()
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
