package cl.playground.scommerce.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class QuotationExceptionNotFound extends RuntimeException {
    public QuotationExceptionNotFound(String message) {
        super(message);
    }
}