package cl.playground.scommerce.controller.quotations;

import cl.playground.scommerce.commands.quotations.CreateQuotationCommand;
import cl.playground.scommerce.commands.quotations.DeleteQuotationCommand;
import cl.playground.scommerce.commands.quotations.UpdateQuotationCommand;
import cl.playground.scommerce.handler.QuotationCommandHandler;

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
