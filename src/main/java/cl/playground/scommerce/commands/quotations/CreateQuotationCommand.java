package cl.playground.scommerce.commands.quotations;

import java.util.List;

public class CreateQuotationCommand {

    private List<CreateQuotationItemCommand> items;

    public CreateQuotationCommand() {}

    public CreateQuotationCommand(List<CreateQuotationItemCommand> items) {
        this.items = items;
    }

    public List<CreateQuotationItemCommand> getItems() {
        return items;
    }

    public void setItems(List<CreateQuotationItemCommand> items) {
        this.items = items;
    }
}
