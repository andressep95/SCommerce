package cl.playground.scommerce.service.export;

import java.io.OutputStream;
import java.util.List;

public interface ExportService {

    void export(List<?> data, OutputStream outputStream) throws Exception;

}
