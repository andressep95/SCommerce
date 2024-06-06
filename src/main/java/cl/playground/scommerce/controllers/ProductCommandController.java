package cl.playground.scommerce.controllers;

import cl.playground.scommerce.commands.CreateProductCommand;
import cl.playground.scommerce.commands.DeleteProductCommand;
import cl.playground.scommerce.commands.UpdateProductCommand;
import cl.playground.scommerce.handlers.ProductCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductCommandController {

    private final ProductCommandHandler commandHandler;

    @Autowired
    public ProductCommandController(ProductCommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @PostMapping
    public void createProduct(@RequestBody CreateProductCommand command) {
        commandHandler.handle(command);
    }

    @PutMapping("/{id}")
    public void updateProduct(@PathVariable Integer id, @RequestBody UpdateProductCommand command) {
        command.setId(id);
        commandHandler.handle(command);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Integer id, @RequestBody DeleteProductCommand command) {
        command.setId(id);
        commandHandler.handle(command);
    }
}
