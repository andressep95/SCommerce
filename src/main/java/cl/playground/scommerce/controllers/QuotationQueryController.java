package cl.playground.scommerce.controllers;

import cl.playground.scommerce.dtos.QuotationDTO;
import cl.playground.scommerce.query.QuotationQueryHandler;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/quotations")
public class QuotationQueryController {

    private final QuotationQueryHandler queryHandler;

    public QuotationQueryController(QuotationQueryHandler queryHandler) {
        this.queryHandler = queryHandler;
    }

    @GetMapping
    public List<QuotationDTO> getAllQuotations(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        return queryHandler.getAllQuotations(page, size);
    }

    @GetMapping("/{id}")
    public Optional<QuotationDTO> getQuotationById(@PathVariable int id) {
        return queryHandler.getQuotationById(id);
    }
}
