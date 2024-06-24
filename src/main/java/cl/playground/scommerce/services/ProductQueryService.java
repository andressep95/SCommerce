package cl.playground.scommerce.services;

import cl.playground.scommerce.dtos.ProductDTO;
import cl.playground.scommerce.entities.Product;
import cl.playground.scommerce.exceptions.ProductExceptionNotFound;
import cl.playground.scommerce.mappers.ProductMapper;
import cl.playground.scommerce.repositories.IProductRepository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductQueryService {

    private final IProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductQueryService(IProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Cacheable(value = "products")
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAllProducts().stream()
        .map(productMapper::mapToDTO)
        .collect(Collectors.toList());
    }

    @Cacheable(value = "products", key = "#id")
    public ProductDTO getProductById(Integer id) {
        Optional<Product> product = productRepository.findProductById(id);
        return product.map(productMapper::mapToDTO)
        .orElseThrow(() -> new ProductExceptionNotFound("Product not found"));
    }
}
