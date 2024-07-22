package cl.playground.scommerce.handler;

import cl.playground.scommerce.commands.quotations.CreateQuotationCommand;
import cl.playground.scommerce.commands.quotations.DeleteQuotationCommand;
import cl.playground.scommerce.commands.quotations.UpdateQuotationCommand;
import cl.playground.scommerce.config.RedisConnection;
import cl.playground.scommerce.service.command.QuotationCommandService;

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
