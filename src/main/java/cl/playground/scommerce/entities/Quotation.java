package cl.playground.scommerce.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quotations")
public class Quotation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Timestamp createdAt;
    private double total;
    @OneToMany(mappedBy = "quotation", cascade = CascadeType.ALL)
    private List<QuotationItem> items;

    public Quotation() {
        this.items = new ArrayList<>();
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

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<QuotationItem> getItems() {
        return items;
    }

    public void setItems(List<QuotationItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Quotation{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", total=" + total +
                ", items=" + items +
                '}';
    }
}
