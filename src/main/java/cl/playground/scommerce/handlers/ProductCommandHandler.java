package cl.playground.scommerce.handlers;

import cl.playground.scommerce.commands.CreateProductCommand;
import cl.playground.scommerce.commands.DeleteProductCommand;
import cl.playground.scommerce.commands.UpdateProductCommand;
import cl.playground.scommerce.configurations.RedisConnection;
import cl.playground.scommerce.entities.Product;
import cl.playground.scommerce.repositories.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductCommandHandler {

    private final IProductRepository productRepository;
    private final RedisConnection redisConnection;

    @Autowired
    public ProductCommandHandler(IProductRepository productRepository, RedisConnection redisConnection) {
        this.productRepository = productRepository;
        this.redisConnection = redisConnection;
    }

    public void handle(CreateProductCommand command) {
        Product product = new Product(command.getName(), command.getPrice());
        productRepository.save(product);
        redisConnection.clearCache("products");
    }

    public void handle(UpdateProductCommand command) {
        Optional<Product> optionalProduct = productRepository.findById(command.getId());
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setName(command.getName());
            product.setPrice(command.getPrice());
            productRepository.save(product);
            redisConnection.clearCache("products");
        }
    }

    public void handle(DeleteProductCommand command) {
        productRepository.deleteById(command.getId());
        redisConnection.clearCache("products");
    }
}
