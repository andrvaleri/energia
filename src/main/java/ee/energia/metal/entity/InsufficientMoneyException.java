package ee.energia.metal.entity;

public class InsufficientMoneyException extends BakeSaleException {
    public InsufficientMoneyException() {
        super("Not Enough Money.");
    }
}
