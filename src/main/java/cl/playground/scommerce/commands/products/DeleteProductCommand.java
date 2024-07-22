package cl.playground.scommerce.commands.products;

public class DeleteProductCommand {
    private Integer id;

    public DeleteProductCommand() {}

    public DeleteProductCommand(int id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
