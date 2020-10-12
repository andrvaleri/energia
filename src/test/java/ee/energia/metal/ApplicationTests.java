package ee.energia.metal;

import ee.energia.metal.dataprovider.BakeGoodRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ApplicationTests {
    @Autowired
    private BakeGoodRepository bakeGoodRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void testInitDatabase() {
        assertTrue(bakeGoodRepository.findAll().size() > 0);
    }


}
