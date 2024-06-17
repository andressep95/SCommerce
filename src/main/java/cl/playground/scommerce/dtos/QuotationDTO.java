package cl.playground.scommerce.dtos;

import java.sql.Timestamp;
import java.util.List;


public class QuotationDTO {
    private int id;
    private Timestamp createdAt;
    private double total;
    private List<QuotationItemDTO> items;
    public QuotationDTO() {
    }
    public QuotationDTO(int id, Timestamp createdAt, double total, List<QuotationItemDTO> items) {
        this.id = id;
        this.createdAt = createdAt;
        this.total = total;
        this.items = items;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(java.sql.Timestamp timestamp) {
        this.createdAt = timestamp;
    }
    public double getTotal() {
        return total;
    }
    public void setTotal(double total) {
        this.total = total;
    }
    public List<QuotationItemDTO> getItems() {
        return items;
    }
    public void setItems(List<QuotationItemDTO> items) {
        this.items = items;
    }


    
}
