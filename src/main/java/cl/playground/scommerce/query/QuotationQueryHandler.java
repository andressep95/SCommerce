package cl.playground.scommerce.query;

import cl.playground.scommerce.dtos.QuotationDTO;
import cl.playground.scommerce.services.QuotationQueryService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class QuotationQueryHandler {
    private final QuotationQueryService quotationQueryService;

    public QuotationQueryHandler(QuotationQueryService quotationQueryService) {
        this.quotationQueryService = quotationQueryService;
    }

    public List<QuotationDTO> getAllQuotations(int page, int size) {
        return quotationQueryService.getAllQuotations(page, size);
    }

    public Optional<QuotationDTO> getQuotationById(Integer id) {
        return Optional.ofNullable(quotationQueryService.getQuotationById(id));
    }
}
