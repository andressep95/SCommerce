package cl.playground.scommerce.service.query;

import cl.playground.scommerce.dtos.ProductDTO;
import cl.playground.scommerce.entity.Product;
import cl.playground.scommerce.exception.ProductNotFoundException;
import cl.playground.scommerce.mapper.ProductMapper;
import cl.playground.scommerce.repository.IProductRepository;

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

    public List<Product> getProductForReport() {
        return productRepository.getProductsForReport();
    }

    @Cacheable(value = "products")
    public List<ProductDTO> getAllProducts(int page, int size) {
        return productRepository.findAllProducts(page, size).stream()
                .map(productMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "products", key = "#id")
    public ProductDTO getProductById(Integer id) {
        Optional<Product> product = productRepository.findProductById(id);
        return product.map(productMapper::mapToDTO)
        .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }
}
