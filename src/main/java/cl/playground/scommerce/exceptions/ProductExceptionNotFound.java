package cl.playground.scommerce.exceptions;


public class ProductExceptionNotFound extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ProductExceptionNotFound(String message) {
        super(message);
    }
}
