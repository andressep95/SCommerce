package cl.playground.scommerce.services;

import cl.playground.scommerce.commands.CreateQuotationCommand;
import cl.playground.scommerce.commands.CreateQuotationItemCommand;
import cl.playground.scommerce.commands.DeleteQuotationCommand;
import cl.playground.scommerce.commands.UpdateQuotationCommand;
import cl.playground.scommerce.entities.Product;
import cl.playground.scommerce.entities.Quotation;
import cl.playground.scommerce.entities.QuotationItem;
import cl.playground.scommerce.exceptions.ProductNotFoundException;
import cl.playground.scommerce.exceptions.QuotationExceptionNotFound;
import cl.playground.scommerce.repositories.IProductRepository;
import cl.playground.scommerce.repositories.IQuotationItemRepository;
import cl.playground.scommerce.repositories.IQuotationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class QuotationCommandService {

    private static final Logger logger = Logger.getLogger(QuotationCommandService.class.getName());
    private final IQuotationRepository quotationRepository;
    private final IQuotationItemRepository quotationItemRepository;
    private final IProductRepository productRepository;

    public QuotationCommandService(IQuotationRepository quotationRepository, IQuotationItemRepository quotationItemRepository, IProductRepository productRepository) {
        this.quotationRepository = quotationRepository;
        this.quotationItemRepository = quotationItemRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void createQuotation(CreateQuotationCommand command) {
        try {
            // Insertar la cotización y obtener el ID generado
            Integer quotationId = quotationRepository.createQuotation();
            if (quotationId == null) {
                throw new RuntimeException("Failed to create quotation");
            }

            // Obtener la cotización recién creada
            Optional<Quotation> optionalQuotation = quotationRepository.findQuotationById(quotationId);
            if (optionalQuotation.isEmpty()) {
                throw new QuotationExceptionNotFound("Quotation not found after creation");
            }
            Quotation quotation = optionalQuotation.get();

            // Guardar los ítems de la cotización
            for (CreateQuotationItemCommand itemCommand : command.getItems()) {
                Optional<Product> product = productRepository.findProductById(itemCommand.getProductId());
                if (product.isPresent()) {
                    QuotationItem quotationItem = new QuotationItem();
                    quotationItem.setQuotation(quotation);
                    quotationItem.setProduct(product.get());
                    quotationItem.setQuantity(itemCommand.getQuantity());
                    quotationItemRepository.createQuotationItem(
                            quotationItem.getQuotation().getId(),
                            quotationItem.getProduct().getId(),
                            quotationItem.getQuantity());
                } else {
                    throw new ProductNotFoundException("Product with ID " + itemCommand.getProductId() + " not found");
                }
            }
        } catch (RuntimeException e) {
            logger.log(Level.WARNING,"Unexpected error creating quotation: " + e.getMessage());
            throw e;
        }

    }

    @Transactional
    public void updateQuotation(UpdateQuotationCommand command) {
        try {
            Optional<Quotation> optionalQuotation = quotationRepository.findQuotationById(command.getId());
            if (optionalQuotation.isPresent()) {
                Quotation quotation = optionalQuotation.get();
                logger.info("Updating quotation with ID " + quotation.getId());

                // Eliminar ítems existentes de la cotización
                quotationItemRepository.deleteByQuotationId(quotation.getId());
                // Agregar los nuevos ítems a la cotización
                for (CreateQuotationItemCommand itemCommand : command.getItems()) {
                    Optional<Product> product = productRepository.findProductById(itemCommand.getProductId());
                    if (product.isPresent()) {
                        QuotationItem quotationItem = new QuotationItem();
                        quotationItem.setQuotation(quotation);
                        quotationItem.setProduct(product.get());
                        quotationItem.setQuantity(itemCommand.getQuantity());
                        quotationItemRepository.createQuotationItem(
                                quotationItem.getQuotation().getId(),
                                quotationItem.getProduct().getId(),
                                quotationItem.getQuantity());
                    } else {
                        throw new ProductNotFoundException("Product with ID " + itemCommand.getProductId() + " not found");
                    }
                }
            } else {
                throw new QuotationExceptionNotFound("Quotation with ID " + command.getId() + " not found");
            }
        } catch (RuntimeException e) {
            logger.log(Level.WARNING,"Unexpected error updating quotation: " + e.getMessage());
            throw e;
        }
    }

    public void deleteQuotation(DeleteQuotationCommand command) {
        try {
            Optional<Quotation> optionalQuotation = quotationRepository.findQuotationById(command.getId());
            if (optionalQuotation.isPresent()) {
                quotationItemRepository.deleteByQuotationId(command.getId());
                quotationRepository.deleteQuotation(command.getId());
            } else {
                throw new QuotationExceptionNotFound("Quotation with ID " + command.getId() + " not found");
            }
        } catch (RuntimeException e) {
            logger.log(Level.WARNING,"Unexpected error deleting quotation: " + e.getMessage());
            throw e;
        }
    }
}
