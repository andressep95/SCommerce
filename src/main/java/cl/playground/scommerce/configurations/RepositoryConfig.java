package cl.playground.scommerce.configurations;

import cl.playground.scommerce.repositories.NativeProductRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class RepositoryConfig {

    @Bean
    public NativeProductRepository nativeProductRepository(DataSource dataSource) {
        return new NativeProductRepository(dataSource);
    }
}
