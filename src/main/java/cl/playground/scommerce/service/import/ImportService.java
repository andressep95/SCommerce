package cl.playground.scommerce.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImportService {
    void importData(MultipartFile file) throws IOException;
}
