package cl.playground.scommerce.service.command;

import cl.playground.scommerce.commands.products.CreateProductCommand;
import cl.playground.scommerce.commands.products.DeleteProductCommand;
import cl.playground.scommerce.commands.products.UpdateProductCommand;
import cl.playground.scommerce.entity.Product;
import cl.playground.scommerce.exception.DatabaseException;
import cl.playground.scommerce.exception.DuplicateProductException;
import cl.playground.scommerce.exception.InvalidProductException;
import cl.playground.scommerce.exception.ProductNotFoundException;
import cl.playground.scommerce.repository.IProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ProductCommandService {

    private static final Logger logger = Logger.getLogger(ProductCommandService.class.getName());
    private final IProductRepository productRepository;

    public ProductCommandService(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public void createProduct(CreateProductCommand command) {
        if (command.getName() == null || command.getName().isEmpty()) {
            throw new InvalidProductException("Product name cannot be null or empty");
        }
        if (command.getPrice() <= 0) {
            throw new InvalidProductException("Product price must be greater than 0");
        }
        if (productRepository.findProductByName(command.getName()).isPresent()) {
            throw new DuplicateProductException("Product already exists with name: " + command.getName());
        }
        try {
            productRepository.createProduct(command.getName(), command.getPrice());
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Unexpected error creating product with name: " + command.getName(), e);
            throw new DatabaseException("Error creating product with name: " + command.getName());
        }
    }

    @Transactional
    public void updateProduct(UpdateProductCommand command) {
        if (command.getName() == null || command.getName().isEmpty()) {
            throw new InvalidProductException("Product name cannot be null or empty");
        }
        if (command.getPrice() <= 0) {
            throw new InvalidProductException("Product price must be greater than 0");
        }
        if (command.getId() < 1) {
            throw new InvalidProductException("Product ID cannot be null");
        }
        Product product = productRepository.findProductById(command.getId())
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + command.getId() + " not found"));

        product.setName(command.getName());
        product.setPrice(command.getPrice());
        try {
            productRepository.updateProduct(product.getId(), product.getName(), product.getPrice());
        } catch (DatabaseException e) {
            logger.log(Level.WARNING, "Error updating product with ID: " + command.getId(), e);
            throw new DatabaseException("Error updating product with ID: " + command.getId());
        }
    }

    @Transactional
    public void deleteProduct(DeleteProductCommand command) {
        Product product = productRepository.findProductById(command.getId())
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + command.getId() + " not found"));
        try {
            productRepository.deleteProduct(product.getId());
        } catch (DatabaseException e) {
            logger.log(Level.WARNING, "Error deleting product with ID: " + command.getId(), e);
            throw new DatabaseException("Error deleting product with ID: " + command.getId());
        }
    }

    // utilizar un object o los tipos de datos en especificos de los campos a validar.
    private void validationProduct() {

    }
}