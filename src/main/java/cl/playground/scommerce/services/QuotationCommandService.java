package cl.playground.scommerce.services;

import cl.playground.scommerce.commands.CreateQuotationCommand;
import cl.playground.scommerce.commands.CreateQuotationItemCommand;
import cl.playground.scommerce.commands.DeleteQuotationCommand;
import cl.playground.scommerce.commands.UpdateQuotationCommand;
import cl.playground.scommerce.entities.Product;
import cl.playground.scommerce.entities.Quotation;
import cl.playground.scommerce.entities.QuotationItem;
import cl.playground.scommerce.repositories.IProductRepository;
import cl.playground.scommerce.repositories.IQuotationItemRepository;
import cl.playground.scommerce.repositories.IQuotationRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class QuotationCommandService {

    private final IQuotationRepository quotationRepository;
    private final IQuotationItemRepository quotationItemRepository;
    private final IProductRepository productRepository;

    public QuotationCommandService(IQuotationRepository quotationRepository, IQuotationItemRepository quotationItemRepository, IProductRepository productRepository) {
        this.quotationRepository = quotationRepository;
        this.quotationItemRepository = quotationItemRepository;
        this.productRepository = productRepository;
    }

    public void createQuotation(CreateQuotationCommand command) {
        Quotation quotation = new Quotation();
        quotation.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        quotationRepository.save(quotation);

        for (CreateQuotationItemCommand itemCommand : command.getItems()) {
            Optional<Product> product = productRepository.findById(itemCommand.getProductId());
            if (product.isPresent()) {
                QuotationItem quotationItem = new QuotationItem();
                quotationItem.setQuotation(quotation);
                quotationItem.setProduct(product.get());
                quotationItem.setQuantity(itemCommand.getQuantity());
                quotationItemRepository.save(quotationItem);

            } else {
                throw new RuntimeException("Product not found");
            }
        }

        // Actualizar el total de la cotización
        quotationRepository.save(quotation);
    }

    public void updateQuotation(UpdateQuotationCommand command) {
        Optional<Quotation> optionalQuotation = quotationRepository.findById(command.getId());
        if (optionalQuotation.isPresent()) {
            Quotation quotation = optionalQuotation.get();

            // Eliminar ítems existentes de la cotización
            quotationItemRepository.deleteByQuotationId(quotation.getId());

            // Agregar los nuevos ítems a la cotización
            for (CreateQuotationItemCommand itemCommand : command.getItems()) {
                Optional<Product> product = productRepository.findById(itemCommand.getProductId());
                if (product.isPresent()) {
                    QuotationItem quotationItem = new QuotationItem();
                    quotationItem.setQuotation(quotation);
                    quotationItem.setProduct(product.get());
                    quotationItem.setQuantity(itemCommand.getQuantity());
                    quotationItemRepository.save(quotationItem);

                } else {
                    throw new RuntimeException("Product not found");
                }
            }

            // Actualizar el total de la cotización
            quotationRepository.save(quotation);
        } else {
            throw new RuntimeException("Quotation not found");
        }
    }

    public void deleteQuotation(DeleteQuotationCommand command) {
        quotationRepository.deleteById(command.getId());
    }
}
