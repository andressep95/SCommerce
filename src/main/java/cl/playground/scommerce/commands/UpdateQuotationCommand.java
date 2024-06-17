package cl.playground.scommerce.commands;

import java.util.List;

public class UpdateQuotationCommand {

    private int id;
    private List<CreateQuotationItemCommand> items;

    public UpdateQuotationCommand() {}

    public UpdateQuotationCommand(int id, List<CreateQuotationItemCommand> items) {
        this.id = id;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<CreateQuotationItemCommand> getItems() {
        return items;
    }

    public void setItems(List<CreateQuotationItemCommand> items) {
        this.items = items;
    }
}
