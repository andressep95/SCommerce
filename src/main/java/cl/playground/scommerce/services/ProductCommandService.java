package cl.playground.scommerce.services;

import cl.playground.scommerce.commands.CreateProductCommand;
import cl.playground.scommerce.commands.DeleteProductCommand;
import cl.playground.scommerce.commands.UpdateProductCommand;
import cl.playground.scommerce.entities.Product;
import cl.playground.scommerce.exceptions.ProductExceptionNotFound;
import cl.playground.scommerce.repositories.IProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ProductCommandService {

    private static final Logger logger = Logger.getLogger(ProductCommandService.class.getName());
    private final IProductRepository productRepository;

    public ProductCommandService(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void createProduct(CreateProductCommand command) {
        try {
            Product product = new Product(command.getName(), command.getPrice());
            productRepository.createProduct(product.getName(), product.getPrice());
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Unexpected error creating product with name: " + command.getName(), e);
            throw new RuntimeException("Error creating product with name: " + command.getName(), e);
        }
    }

    @Transactional
    public void updateProduct(UpdateProductCommand command) {
        try {
            Optional<Product> optionalProduct = productRepository.findById(command.getId());
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                product.setId(command.getId());
                product.setName(command.getName());
                product.setPrice(command.getPrice());
                productRepository.updateProduct(product.getId(), product.getName(), product.getPrice());
            } else {
                throw new ProductExceptionNotFound("Product with ID " + command.getId() + " not found");
            }
        } catch (ProductExceptionNotFound e) {
            logger.log(Level.WARNING, "Error updating product with ID: " + command.getId(), e);
            throw new ProductExceptionNotFound("Error updating product with ID: " + command.getId());
        }
    }

    @Transactional
    public void deleteProduct(DeleteProductCommand command) {
        try {
            Optional<Product> optionalProduct = productRepository.findById(command.getId());
            if (optionalProduct.isPresent()) {
                productRepository.deleteProduct(command.getId());
            } else {
                throw new ProductExceptionNotFound("Product with ID " + command.getId() + " not found");
            }
        } catch (ProductExceptionNotFound e) {
            logger.log(Level.WARNING, "Error deleting product with ID: " + command.getId(), e);
            throw new ProductExceptionNotFound("Error deleting product with ID: " + command.getId());
        }
    }
}