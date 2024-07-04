package cl.playground.scommerce.controllers;

import cl.playground.scommerce.services.CsvImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/import")
public class CsvImportController {

    @Autowired
    private CsvImportService csvImportService;

    @PostMapping("/products")
    public ResponseEntity<String> importProducts(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty.");
        }
        try {
            csvImportService.importProducts(file);
            return ResponseEntity.ok("Products imported successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import products: " + e.getMessage());
        }
    }

    @PostMapping("/quotations")
    public ResponseEntity<String> importQuotations(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty.");
        }
        try {
            csvImportService.importQuotations(file);
            return ResponseEntity.ok("Quotations imported successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import quotations: " + e.getMessage());
        }
    }
}
