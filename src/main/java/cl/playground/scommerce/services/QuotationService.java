package cl.playground.scommerce.services;

import cl.playground.scommerce.entities.Quotation;
import cl.playground.scommerce.entities.QuotationItem;
import cl.playground.scommerce.repositories.IQuotationItemRepository;
import cl.playground.scommerce.repositories.IQuotationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuotationService {

    @Autowired
    IQuotationRepository quotationRepository;
    @Autowired
    IQuotationItemRepository quotationItemRepository;

    public List<Quotation> getAllQuotations() {
        return quotationRepository.findAll();
    }

    public Quotation getQuotationById(int id) {
        return quotationRepository.findById(id).orElse(null);
    }

    public Quotation createQuotation(Quotation quotation) {
        Quotation savedQuotation = quotationRepository.save(quotation);
        for (QuotationItem item : quotation.getItems()) {
            item.setQuotation(savedQuotation);
            quotationItemRepository.save(item);
        }
        return savedQuotation;
    }

    public Quotation updateQuotation(Quotation quotation) {
        return quotationRepository.save(quotation);
    }

    public void deleteQuotation(int id) {
        quotationRepository.deleteById(id);
    }
}
