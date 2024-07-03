package cl.playground.scommerce.configurations;

import cl.playground.scommerce.repositories.NativeProductRepository;
import cl.playground.scommerce.repositories.NativeQuotationItemRepository;
import cl.playground.scommerce.repositories.NativeQuotationRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class RepositoryConfig {
    @Bean
    public NativeProductRepository nativeProductRepository(DataSource dataSource) {
        return new NativeProductRepository(dataSource);
    }
    @Bean
    public NativeQuotationRepository quotationRepository(DataSource dataSource, NativeQuotationItemRepository nativeQuotationItemRepository) {
        return new NativeQuotationRepository(dataSource, nativeQuotationItemRepository);
    }
    @Bean
    public NativeQuotationItemRepository nativeQuotationItemRepository(DataSource dataSource) {
        return new NativeQuotationItemRepository(dataSource);
    }
}
