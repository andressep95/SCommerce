package cl.playground.scommerce.commands;

public class CreateQuotationItemCommand {
    private int productId;
    private int quantity;

    public CreateQuotationItemCommand() {}

    public CreateQuotationItemCommand(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}