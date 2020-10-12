package ee.energia.metal.entity;

public class NotEnoughStockException extends BakeSaleException {
    public NotEnoughStockException() {
        super("Not Enough Stock.");
    }
}
