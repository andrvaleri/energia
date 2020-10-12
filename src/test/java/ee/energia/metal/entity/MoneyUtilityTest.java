package ee.energia.metal.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MoneyUtilityTest {

    @Test
    void testTransformMoney() {
        assertThrows(BakeSaleException.class, () -> MoneyUtility.transformMoney("money"));
        assertEquals(new BigDecimal("4.0"), MoneyUtility.transformMoney("$4.0"));
        assertEquals(new BigDecimal("4.0"), MoneyUtility.transformMoney("4.0"));
        assertEquals(new BigDecimal("0.40"), MoneyUtility.transformMoney("40c"));
    }
}
