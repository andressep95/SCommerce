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

}
