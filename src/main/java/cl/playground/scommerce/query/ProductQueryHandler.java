package cl.playground.scommerce.query;

import cl.playground.scommerce.dtos.ProductDTO;
import cl.playground.scommerce.entities.Product;
import cl.playground.scommerce.repositories.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductQueryHandler {
    private final IProductRepository productRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public ProductQueryHandler(IProductRepository productRepository, RedisTemplate<String, Object> redisTemplate) {
        this.productRepository = productRepository;
        this.redisTemplate = redisTemplate;
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(Integer id) {
        return productRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    private ProductDTO convertToDTO(Product product) {
        return new ProductDTO(product.getId(), product.getName(), product.getPrice());
    }
}
