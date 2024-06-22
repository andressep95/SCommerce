package cl.playground.scommerce.services;

import cl.playground.scommerce.commands.CreateProductCommand;
import cl.playground.scommerce.commands.UpdateProductCommand;
import cl.playground.scommerce.commands.DeleteProductCommand;
import cl.playground.scommerce.entities.Product;
import cl.playground.scommerce.repositories.IProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductCommandService {

    private final IProductRepository productRepository;

    public ProductCommandService(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void createProduct(CreateProductCommand command) {
        Product product = new Product(command.getName(), command.getPrice());
        productRepository.createProduct(product.getName(), product.getPrice());
    }

    public void updateProduct(UpdateProductCommand command) {
        Optional<Product> optionalProduct = productRepository.findById(command.getId());
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setId(command.getId());
            product.setName(command.getName());
            product.setPrice(command.getPrice());
            productRepository.updateProduct(product.getId(), product.getName(), product.getPrice());
        }
    }

    public void deleteProduct(DeleteProductCommand command) {

        productRepository.deleteProduct(command.getId());
    }
}
