package cl.playground.scommerce.mappers;

import org.springframework.stereotype.Component;

import cl.playground.scommerce.dtos.ProductDTO;
import cl.playground.scommerce.entities.Product;

@Component
public class ProductMapper {
    
        public ProductDTO mapToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        return dto;
    }
    public Product mapToEntity(ProductDTO dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        return product;
    }

}
