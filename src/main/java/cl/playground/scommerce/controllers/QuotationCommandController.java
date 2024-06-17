package cl.playground.scommerce.controllers;

import cl.playground.scommerce.commands.CreateQuotationCommand;
import cl.playground.scommerce.commands.DeleteQuotationCommand;
import cl.playground.scommerce.commands.UpdateQuotationCommand;
import cl.playground.scommerce.handlers.QuotationCommandHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/quotations")
public class QuotationCommandController {

    private final QuotationCommandHandler commandHandler;

    public QuotationCommandController(QuotationCommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @PostMapping
    public void createQuotation(@RequestBody CreateQuotationCommand command) {
        commandHandler.handle(command);
    }

    @PutMapping("/{id}")
    public void updateQuotation(@PathVariable Integer id, @RequestBody UpdateQuotationCommand command) {
        command.setId(id);
        commandHandler.handle(command);
    }

    @DeleteMapping("/{id}")
    public void deleteQuotation(@PathVariable Integer id, @RequestBody DeleteQuotationCommand command) {
        command.setId(id);
        commandHandler.handle(command);
    }
}
