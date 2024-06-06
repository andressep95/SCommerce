package cl.playground.scommerce.controllers;

import cl.playground.scommerce.dtos.ProductDTO;
import cl.playground.scommerce.query.ProductQueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductQueryController {

    private final ProductQueryHandler productQueryHandler;

    @Autowired
    public ProductQueryController(ProductQueryHandler productQueryHandler) {
        this.productQueryHandler = productQueryHandler;
    }

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productQueryHandler.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable Integer id) {
        return productQueryHandler.getProductById(id);
    }
}
