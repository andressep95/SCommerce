package cl.playground.scommerce.commands;

import cl.playground.scommerce.entities.QuotationItem;

import java.util.List;

public class CreateQuotationCommand {
    private List<QuotationItem> items;

    public CreateQuotationCommand() {}

    public CreateQuotationCommand(List<QuotationItem> items) {
        this.items = items;
    }

    public List<QuotationItem> getItems() {
        return items;
    }

    public void setItems(List<QuotationItem> items) {
        this.items = items;
    }
}
