package ee.energia.metal.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BakeGoodTest {
    private final BakeGood bakeGood = new BakeGood();

    @Test
    void testSetPrice() {
        bakeGood.setPrice("$2.06");
        assertEquals(new BigDecimal("2.06"), bakeGood.getPrice());
        bakeGood.setPrice("65c");
        assertEquals(new BigDecimal("0.65"), bakeGood.getPrice());
    }

    @Test
    void testReduceAmount() {
        bakeGood.setAmount(2L);
        bakeGood.reduceAmount();
        assertEquals(1L, bakeGood.getAmount());
        bakeGood.reduceAmount();
        assertEquals(0, bakeGood.getAmount());
        bakeGood.reduceAmount();
        assertEquals(0, bakeGood.getAmount());
    }

}