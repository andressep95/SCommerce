package cl.playground.scommerce.commands;

public class DeleteQuotationCommand {
    private int id;

    public DeleteQuotationCommand() {}

    public DeleteQuotationCommand(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
