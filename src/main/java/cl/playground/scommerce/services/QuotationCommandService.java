package cl.playground.scommerce.services;

import cl.playground.scommerce.commands.CreateQuotationCommand;
import cl.playground.scommerce.commands.DeleteQuotationCommand;
import cl.playground.scommerce.commands.UpdateQuotationCommand;
import cl.playground.scommerce.entities.Quotation;
import cl.playground.scommerce.entities.QuotationItem;
import cl.playground.scommerce.repositories.IQuotationItemRepository;
import cl.playground.scommerce.repositories.IQuotationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuotationCommandService {

    private final IQuotationRepository quotationRepository;
    private final IQuotationItemRepository quotationItemRepository;

    public QuotationCommandService(IQuotationRepository quotationRepository, IQuotationItemRepository quotationItemRepository) {
        this.quotationRepository = quotationRepository;
        this.quotationItemRepository = quotationItemRepository;
    }

    public void createQuotation(CreateQuotationCommand command) {
        Quotation quotation = new Quotation();
        quotationRepository.save(quotation);

        for (QuotationItem item : command.getItems()) {
            item.setQuotation(quotation);
            quotationItemRepository.save(item);
        }
    }

    public void updateQuotation(UpdateQuotationCommand command) {
        Optional<Quotation> optionalQuotation = quotationRepository.findById(command.getId());
        if (optionalQuotation.isPresent()) {
            Quotation quotation = optionalQuotation.get();

            // Eliminar ítems existentes de la cotización
            quotationItemRepository.deleteByQuotationId(quotation.getId());

            // Agregar los nuevos ítems a la cotización
            List<QuotationItem> newItems = command.getItems();
            for (QuotationItem item : newItems) {
                item.setQuotation(quotation);
                quotationItemRepository.save(item);
            }

            // Actualizar la entidad de la cotización en la base de datos si es necesario
            quotationRepository.save(quotation);
        } else {
            throw new RuntimeException("Quotation not found");
        }
    }

    public void deleteQuotation(DeleteQuotationCommand command) {
        quotationRepository.deleteById(command.getId());
    }
}