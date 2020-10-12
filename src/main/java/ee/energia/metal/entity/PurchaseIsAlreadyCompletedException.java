package ee.energia.metal.entity;

public class PurchaseIsAlreadyCompletedException extends BakeSaleException {
    public PurchaseIsAlreadyCompletedException() {
        super("Purchase is already completed");
    }
}
