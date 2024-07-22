package cl.playground.scommerce.mapper;

import cl.playground.scommerce.dtos.QuotationDTO;
import cl.playground.scommerce.entity.Quotation;

import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class QuotationMapper {

    private final QuotationItemMapper quotationItemMapper;

    public QuotationMapper(QuotationItemMapper quotationItemMapper) {
        this.quotationItemMapper = quotationItemMapper;
    }

    public QuotationDTO mapToDTO(Quotation quotation) {
        QuotationDTO dto = new QuotationDTO();
        dto.setId(quotation.getId());
        dto.setCreatedAt(quotation.getCreatedAt());
        dto.setTotal(quotation.getTotal());
        dto.setItems(quotation.getItems().stream()
                .map(quotationItemMapper::mapToDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    public Quotation mapToEntity(QuotationDTO dto) {
        Quotation quotation = new Quotation();
        quotation.setId(dto.getId());
        quotation.setCreatedAt(dto.getCreatedAt());
        quotation.setTotal(dto.getTotal());
        quotation.setItems(dto.getItems().stream()
                .map(quotationItemMapper::mapToEntity)
                .collect(Collectors.toList()));
        return quotation;
    }
}
