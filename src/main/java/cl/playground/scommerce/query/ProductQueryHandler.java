package cl.playground.scommerce.query;

import cl.playground.scommerce.dtos.ProductDTO;
import cl.playground.scommerce.services.ProductQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductQueryHandler {
    private final ProductQueryService productQueryService;

    @Autowired
    public ProductQueryHandler(ProductQueryService productQueryService) {
        this.productQueryService = productQueryService;
    }

    public List<ProductDTO> getAllProducts() {
        return productQueryService.getAllProducts();
    }

    public ProductDTO getProductById(Integer id) {
        return productQueryService.getProductById(id);
    }
}
