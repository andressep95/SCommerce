package cl.playground.scommerce.commands;

import cl.playground.scommerce.entities.QuotationItem;
import java.util.List;

public class UpdateQuotationCommand {
    private int id;
    private List<QuotationItem> items;

    public UpdateQuotationCommand() {}

    public UpdateQuotationCommand(int id, List<QuotationItem> items) {
        this.id = id;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<QuotationItem> getItems() {
        return items;
    }

    public void setItems(List<QuotationItem> items) {
        this.items = items;
    }
}
