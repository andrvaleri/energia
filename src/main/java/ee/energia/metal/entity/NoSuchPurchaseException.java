package ee.energia.metal.entity;

public class NoSuchPurchaseException extends BakeSaleException {
    public NoSuchPurchaseException() {
        super("No such purchase exists.");
    }
}
