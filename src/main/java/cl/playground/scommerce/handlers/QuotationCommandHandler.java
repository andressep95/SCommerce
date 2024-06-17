package cl.playground.scommerce.handlers;

import cl.playground.scommerce.commands.CreateQuotationCommand;
import cl.playground.scommerce.commands.DeleteQuotationCommand;
import cl.playground.scommerce.commands.UpdateQuotationCommand;
import cl.playground.scommerce.configurations.RedisConnection;
import cl.playground.scommerce.services.QuotationCommandService;
import org.springframework.stereotype.Component;

@Component
public class QuotationCommandHandler {

    private final QuotationCommandService quotationCommandService;
    private final RedisConnection redisConnection;

    public QuotationCommandHandler(QuotationCommandService quotationCommandService, RedisConnection redisConnection) {
        this.quotationCommandService = quotationCommandService;
        this.redisConnection = redisConnection;
    }

    public void handle(CreateQuotationCommand command) {
        quotationCommandService.createQuotation(command);
        redisConnection.clearCache("quotations");
    }

    public void handle(UpdateQuotationCommand command) {
        quotationCommandService.updateQuotation(command);
        redisConnection.clearCache("quotations");
    }

    public void handle(DeleteQuotationCommand command) {
        quotationCommandService.deleteQuotation(command);
        redisConnection.clearCache("quotations");
    }
}
