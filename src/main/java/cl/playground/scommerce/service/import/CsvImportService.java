package cl.playground.scommerce.service;

import cl.playground.scommerce.entity.Product;
import cl.playground.scommerce.repository.NativeProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvImportService implements ImportService {
    @Autowired
    private NativeProductRepository nativeProductRepository;
    @Override
    public void importData(MultipartFile file) throws IOException {
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
}