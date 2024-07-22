package cl.playground.scommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cl.playground.scommerce.repository.ProductRepository;
import cl.playground.scommerce.repository.QuotationItemRepository;
import cl.playground.scommerce.repository.QuotationRepository;

import javax.sql.DataSource;

@Configuration
public class RepositoryConfig {
    @Bean
    public ProductRepository nativeProductRepository(DataSource dataSource) {
        return new ProductRepository(dataSource);
    }
    @Bean
    public QuotationRepository quotationRepository(DataSource dataSource, QuotationItemRepository nativeQuotationItemRepository) {
        return new QuotationRepository(dataSource, nativeQuotationItemRepository);
    }
    @Bean
    public QuotationItemRepository nativeQuotationItemRepository(DataSource dataSource) {
        return new QuotationItemRepository(dataSource);
    }
}
