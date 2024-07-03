package cl.playground.scommerce.controllers;

import cl.playground.scommerce.dtos.ProductDTO;
import cl.playground.scommerce.query.ProductQueryHandler;
import jakarta.validation.constraints.Min;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductQueryController {

    private final ProductQueryHandler productQueryHandler;

    public ProductQueryController(ProductQueryHandler productQueryHandler) {
        this.productQueryHandler = productQueryHandler;
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts(@RequestParam(defaultValue = "1") Integer page,
                                                           @RequestParam(defaultValue = "10") Integer size) {
        List<ProductDTO> products = productQueryHandler.getAllProducts(page, size);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable @Min(1) Integer id) {
        ProductDTO product = productQueryHandler.getProductById(id);
        return ResponseEntity.ok(product);
    }
}
