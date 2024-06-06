package cl.playground.scommerce.commands;

public class UpdateQuotationCommand {
    private int id;

    public UpdateQuotationCommand() {}

    public UpdateQuotationCommand(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
