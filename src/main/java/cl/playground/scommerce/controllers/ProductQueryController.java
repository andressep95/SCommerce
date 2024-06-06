package cl.playground.scommerce.controllers;

import cl.playground.scommerce.dtos.ProductDTO;
import cl.playground.scommerce.query.ProductQueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductQueryController {

    private final ProductQueryHandler queryHandler;

    @Autowired
    public ProductQueryController(ProductQueryHandler queryHandler) {
        this.queryHandler = queryHandler;
    }

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return queryHandler.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable Integer id) {
        return queryHandler.getProductById(id);
    }
}