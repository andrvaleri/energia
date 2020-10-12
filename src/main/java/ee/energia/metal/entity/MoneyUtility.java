package ee.energia.metal.entity;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
public class MoneyUtility {
    private MoneyUtility() {
    }

    public static BigDecimal transformMoney(String money) {
        try {
            if (money.contains("c")) {
                return new BigDecimal(money.replace("c", ""))
                        .divide(new BigDecimal("100"), 2, RoundingMode.UNNECESSARY);
            } else {
                return new BigDecimal(money.replace("$", ""));
            }
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            throw new BakeSaleException("Wrong Money Input.");
        }
    }
}
