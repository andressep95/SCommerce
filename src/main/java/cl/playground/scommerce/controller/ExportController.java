package cl.playground.scommerce.controller;

import cl.playground.scommerce.entity.Product;
import cl.playground.scommerce.service.export.ExportService;
import cl.playground.scommerce.service.query.ProductQueryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
public class ExportController {

    @Autowired
    private ProductQueryService productQueryService;

    @Autowired
    @Qualifier("excel")
    private ExportService excelReportExporter;

    @GetMapping("/products/export")
    public ResponseEntity<byte[]> exportProductsToExcel() {
        try {
            List<Product> products = productQueryService.getProductForReport();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            excelReportExporter.export(products, outputStream);

            byte[] reportData = outputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "products.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(reportData);

        } catch (Exception e) {
            // Manejo de errores más detallado
            e.printStackTrace();
            return ResponseEntity.status(500).body(("Error exporting products: " + e.getMessage()).getBytes());
        }
    }
}
