package cl.playground.scommerce.service.export;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import cl.playground.scommerce.entity.Product;

import java.io.OutputStream;
import java.util.List;

@Service("excel")
public class ExcelExportService implements ExportService {

    @Override
    public void export(List<?> data, OutputStream outputStream) throws Exception {
        if (data.isEmpty() || !(data.get(0) instanceof Product)) {
            throw new IllegalArgumentException("Invalid data for Excel export");
        }

        List<Product> products = (List<Product>) data;

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Products");

        // Crear cabeceras
        Row headerRow = sheet.createRow(0);
        createCell(headerRow, 0, "ID");
        createCell(headerRow, 1, "Name");
        createCell(headerRow, 2, "Price");

        // Crear filas de datos
        int rowNum = 1;
        for (Product product : products) {
            Row row = sheet.createRow(rowNum++);
            createCell(row, 0, product.getId().toString());
            createCell(row, 1, product.getName());
            createCell(row, 2, product.getPrice().toString());
        }

        // Escribir el workbook al OutputStream
        workbook.write(outputStream);
        workbook.close();
    }

    private void createCell(Row row, int column, String value) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
    }
}
