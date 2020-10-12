package ee.energia.metal.config;

import ee.energia.metal.endpoint.PurchaseConsoleController;
import ee.energia.metal.service.PurchaseService;
import org.springframework.context.annotation.*;

@Configuration
@Profile("console")
public class ConsoleConfig {
    @Bean
    @DependsOn("dataProvidersConfiguration")
    public PurchaseConsoleController commandLineController(PurchaseService purchaseService) {
        return new PurchaseConsoleController(purchaseService);
    }
}
