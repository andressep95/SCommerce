package cl.playground.scommerce.handler;

import cl.playground.scommerce.commands.products.CreateProductCommand;
import cl.playground.scommerce.commands.products.DeleteProductCommand;
import cl.playground.scommerce.commands.products.UpdateProductCommand;
import cl.playground.scommerce.config.RedisConnection;
import cl.playground.scommerce.service.command.ProductCommandService;

import org.springframework.stereotype.Component;

@Component
public class ProductCommandHandler {

    private final ProductCommandService productCommandService;
    private final RedisConnection redisConnection;

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
