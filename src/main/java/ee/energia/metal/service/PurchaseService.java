package ee.energia.metal.service;

import ee.energia.metal.dataprovider.BakeGoodRepository;
import ee.energia.metal.dataprovider.PurchaseRepository;
import ee.energia.metal.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;

import static ee.energia.metal.entity.MoneyUtility.transformMoney;
import static java.util.Arrays.stream;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final BakeGoodRepository bakeGoodRepository;

    public Purchase createPurchase(String bakeGoods) {
        var purchase = new Purchase();
        var price = createPurchase(bakeGoods, purchase);
        purchase.setPrice(price);
        return purchaseRepository.save(purchase);
    }

    private BigDecimal createPurchase(String bakeGoods, Purchase purchase) {
        return stream(bakeGoods.split(","))
                .map(code -> getBakeGood(purchase, code).getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BakeGood getBakeGood(Purchase purchase, String code) {
        var bakeGood = bakeGoodRepository.findByCode(code.trim())
                .orElseThrow(() -> new NoSuchBakeGoodException(code));
        checkAmount(bakeGood);
        purchase.addBakeGood(bakeGood);
        return bakeGood;
    }

    private void checkAmount(BakeGood bakeGood) {
        if (bakeGood.getAmount() < 1) {
            throw new NotEnoughStockException();
        }
    }

    public Purchase pay(Long id, String money) {
        return calculateChange(id, transformMoney(money));
    }

    private Purchase calculateChange(Long id, BigDecimal transformMoney) {
        var purchase = purchaseRepository.findById(id).orElseThrow(NoSuchPurchaseException::new);
        checkIsCompleted(purchase);
        checkInsufficientMoney(transformMoney, purchase);
        return persistPurchaseChanges(transformMoney, purchase);

    }

    private void checkInsufficientMoney(BigDecimal transformMoney, Purchase purchase) {
        if (purchase.getPrice().compareTo(transformMoney) > 0) {
            throw new InsufficientMoneyException();
        }
    }

    private void checkIsCompleted(Purchase purchase) {
        if (purchase.isCompleted()) {
            throw new PurchaseIsAlreadyCompletedException();
        }
    }

    private Purchase persistPurchaseChanges(BigDecimal transformMoney, Purchase purchase) {
        purchase.getBakeGoods().forEach(bakeGood -> bakeGoodRepository.save(bakeGood.reduceAmount()));
        purchase.calculateChange(transformMoney);
        purchase.complete();
        return purchaseRepository.save(purchase);
    }

    public Collection<Purchase> getIncompletePurchases() {
        return purchaseRepository.findAllByStatus(PurchaseStatus.INITIATED);
    }
}
