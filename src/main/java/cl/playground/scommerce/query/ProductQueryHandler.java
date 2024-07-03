package cl.playground.scommerce.query;

import cl.playground.scommerce.dtos.ProductDTO;
import cl.playground.scommerce.services.ProductQueryService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductQueryHandler {
    private final ProductQueryService productQueryService;

    public ProductQueryHandler(ProductQueryService productQueryService) {
        this.productQueryService = productQueryService;
    }

    public List<ProductDTO> getAllProducts(int page, int size) {
        return productQueryService.getAllProducts(page, size);
    }

    public ProductDTO getProductById(Integer id) {
        return productQueryService.getProductById(id);
    }
}
