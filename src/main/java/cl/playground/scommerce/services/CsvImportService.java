package cl.playground.scommerce.services;

import cl.playground.scommerce.entities.Product;
import cl.playground.scommerce.entities.Quotation;
import cl.playground.scommerce.entities.QuotationItem;
import cl.playground.scommerce.repositories.NativeProductRepository;
import cl.playground.scommerce.repositories.NativeQuotationItemRepository;
import cl.playground.scommerce.repositories.NativeQuotationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CsvImportService {
    @Autowired
    private NativeProductRepository nativeProductRepository;
    @Autowired
    private NativeQuotationRepository nativeQuotationRepository;
    @Autowired
    private NativeQuotationItemRepository nativeQuotationItemRepository;


    public void importProducts(MultipartFile file) throws IOException {
        try (InputStream is = file.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            String line;
            List<Product> products = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                Product product = parseProduct(line);
                products.add(product);
            }

            for (Product product : products) {
                nativeProductRepository.createProduct(product.getName(), product.getPrice());
            }
        }
    }
    private Product parseProduct(String line) {
        String[] fields = line.split(",");
        String name = fields[0];
        double price = Double.parseDouble(fields[1]);
        return new Product(name, price);
    }

    public void importQuotations(MultipartFile file) throws IOException {
        try (InputStream is = file.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            String line;
            List<Quotation> quotations = new ArrayList<>();
            Map<Integer, List<QuotationItem>> quotationItemsMap = new HashMap<>();

            boolean isQuotationSection = false;
            boolean isQuotationItemSection = false;

            while ((line = reader.readLine()) != null) {
                if (line.equalsIgnoreCase("Quotations")) {
                    isQuotationSection = true;
                    isQuotationItemSection = false;
                    continue;
                } else if (line.equalsIgnoreCase("QuotationItems")) {
                    isQuotationSection = false;
                    isQuotationItemSection = true;
                    continue;
                }

                if (isQuotationSection) {
                    Quotation quotation = new Quotation();
                    quotations.add(quotation);
                } else if (isQuotationItemSection) {
                    QuotationItem item = parseQuotationItem(line);
                    quotationItemsMap.computeIfAbsent(item.getQuotation().getId(), k -> new ArrayList<>()).add(item);
                }
            }

            for (Quotation quotation : quotations) {
                int quotationId = nativeQuotationRepository.createQuotation();
                quotation.setId(quotationId);

                List<QuotationItem> items = quotationItemsMap.get(quotation.getId());
                if (items != null) {
                    for (QuotationItem item : items) {
                        item.setQuotation(quotation);
                        nativeQuotationItemRepository.createQuotationItem(item.getQuotation().getId(), item.getProduct().getId(), item.getQuantity());
                    }
                }
            }
        }
    }
    private QuotationItem parseQuotationItem(String line) {
        String[] fields = line.split(",");
        int quotationIndex = Integer.parseInt(fields[0]);
        int productId = Integer.parseInt(fields[1]);
        int quantity = Integer.parseInt(fields[2]);

        Quotation quotation = new Quotation();
        quotation.setId(quotationIndex);

        Product product = new Product();
        product.setId(productId);

        QuotationItem item = new QuotationItem();
        item.setQuotation(quotation);
        item.setProduct(product);
        item.setQuantity(quantity);

        return item;
    }

}