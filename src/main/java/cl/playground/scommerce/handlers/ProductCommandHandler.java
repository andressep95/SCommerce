package cl.playground.scommerce.handlers;

import cl.playground.scommerce.commands.CreateProductCommand;
import cl.playground.scommerce.commands.DeleteProductCommand;
import cl.playground.scommerce.commands.UpdateProductCommand;
import cl.playground.scommerce.configurations.RedisConnection;
import cl.playground.scommerce.services.ProductCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductCommandHandler {

    private final ProductCommandService productCommandService;
    private final RedisConnection redisConnection;

    @Autowired
    public ProductCommandHandler(ProductCommandService productCommandService, RedisConnection redisConnection) {
        this.productCommandService = productCommandService;
        this.redisConnection = redisConnection;
    }

    public void handle(CreateProductCommand command) {
        productCommandService.createProduct(command);
        redisConnection.clearCache("products");
    }

    public void handle(UpdateProductCommand command) {
        productCommandService.updateProduct(command);
        redisConnection.clearCache("products");
    }

    public void handle(DeleteProductCommand command) {
        productCommandService.deleteProduct(command);
        redisConnection.clearCache("products");
    }
}
