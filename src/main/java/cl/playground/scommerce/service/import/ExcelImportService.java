
import cl.playground.scommerce.entity.Product;
import cl.playground.scommerce.repository.IProductRepository;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@Service
public class ExcelImportService {
    private static final Logger log = Logger.getLogger(ExcelImportService.class.getName());

    @Autowired
    private IProductRepository nativeProductRepository;

    public static boolean isValidExcelFile(MultipartFile file) {
        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    public static List<Product> getProductsDataFromExcel(InputStream inputStream) {
        List<Product> products = new ArrayList<>();
        try {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            int rowIndex = 0;
            for (Row row : sheet) {
                if (rowIndex == 0) {
                    // Skip header row
                    rowIndex++;
                    continue;
                }
                Product product = parseProduct(row);
                if (product != null) {
                    products.add(product);
                }
                rowIndex++;
            }
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }

    private static Product parseProduct(Row row) {
        Cell nameCell = row.getCell(0);
        Cell priceCell = row.getCell(1);

        if (nameCell == null || priceCell == null) {
            return null;
        }

        String name = getCellValueAsString(nameCell);
        Double price = getCellValueAsDouble(priceCell);

        if (name != null && !name.trim().isEmpty() && price != null && price > 0) {
            return new Product(name, price);
        } else {
            log.info("Invalid product data: name=" + name + " , price=" + price);
            return null;
        }
    }

    private static String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return null;
        }
    }

    private static Double getCellValueAsDouble(Cell cell) {
        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                try {
                    return Double.parseDouble(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    return null;
                }
            default:
                return null;
        }
    }

    public void saveProductsToDatabase(MultipartFile file) {
        if (isValidExcelFile(file)) {
            try {
                List<Product> products = getProductsDataFromExcel(file.getInputStream());
                for (Product product : products) {
                    nativeProductRepository.createProduct(product.getName(), product.getPrice());
                }
            } catch (IOException e) {
                throw new IllegalArgumentException("The file is not a valid excel file", e);
            }
        } else {
            throw new IllegalArgumentException("Invalid file format. Please upload an Excel file.");
        }
    }
}
