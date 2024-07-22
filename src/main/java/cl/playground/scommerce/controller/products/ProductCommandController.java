package cl.playground.scommerce.controller.products;

import cl.playground.scommerce.commands.products.CreateProductCommand;
import cl.playground.scommerce.commands.products.DeleteProductCommand;
import cl.playground.scommerce.commands.products.UpdateProductCommand;
import cl.playground.scommerce.handler.ProductCommandHandler;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductCommandController {

    private final ProductCommandHandler commandHandler;

    public ProductCommandController(ProductCommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody CreateProductCommand command, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        commandHandler.handle(command);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, @Valid @RequestBody UpdateProductCommand command, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        command.setId(id);
        commandHandler.handle(command);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id, @Valid @RequestBody DeleteProductCommand command, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        command.setId(id);
        commandHandler.handle(command);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
