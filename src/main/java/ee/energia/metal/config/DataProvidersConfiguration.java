package ee.energia.metal.config;

import ee.energia.metal.dataprovider.BakeGoodRepository;
import ee.energia.metal.entity.BakeGood;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;

@Configuration
@Slf4j
@ConfigurationProperties(prefix = "sale")
@RequiredArgsConstructor
public class DataProvidersConfiguration {
    private final BakeGoodRepository repository;
    @Setter
    private Map<String, BakeGood> items;

    @PostConstruct
    public void init() {
        items.forEach((s, bakeGood) ->
                log.info("Loading configuration: {} {}", s, repository.save(bakeGood)));
    }
}
