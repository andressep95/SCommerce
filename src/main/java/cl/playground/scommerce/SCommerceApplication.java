package cl.playground.scommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SCommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SCommerceApplication.class, args);
    }

}
