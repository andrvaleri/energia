package ee.energia.metal.entity;

public class NoSuchBakeGoodException extends BakeSaleException {
    public NoSuchBakeGoodException(String code) {
        super("Not Such BakeGood:" + code + ".");
    }
}
