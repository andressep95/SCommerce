package cl.playground.scommerce.services;

import cl.playground.scommerce.dtos.ProductDTO;
import cl.playground.scommerce.entities.Product;
import cl.playground.scommerce.repositories.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final IProductRepository productRepository;

    @Autowired
    public ProductService(IProductRepository productRepository) {
        this.productRepository = productRepository;
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

    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = new Product(productDTO.getName(), productDTO.getPrice());
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    public ProductDTO updateProduct(Integer id, ProductDTO productDTO) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(productDTO.getName());
                    product.setPrice(productDTO.getPrice());
                    return convertToDTO(productRepository.save(product));
                })
                .orElse(null);
    }

    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }

    private ProductDTO convertToDTO(Product product) {
        return new ProductDTO(product.getId(), product.getName(), product.getPrice());
    }
    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }
}
