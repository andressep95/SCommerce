package cl.playground.scommerce.mapper;

import cl.playground.scommerce.dtos.QuotationItemDTO;
import cl.playground.scommerce.entity.QuotationItem;

import org.springframework.stereotype.Component;

@Component
public class QuotationItemMapper {

    private final ProductMapper productMapper;

    public QuotationItemMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public QuotationItemDTO mapToDTO(QuotationItem item) {
        QuotationItemDTO dto = new QuotationItemDTO();
        dto.setId(item.getId());
        dto.setProduct(productMapper.mapToDTO(item.getProduct()));
        dto.setQuantity(item.getQuantity());
        return dto;
    }

    public QuotationItem mapToEntity(QuotationItemDTO dto) {
        QuotationItem item = new QuotationItem();
        item.setId(dto.getId());
        item.setProduct(productMapper.mapToEntity(dto.getProduct()));
        item.setQuantity(dto.getQuantity());
        return item;
    }
}
