package ee.energia.metal.service;

import ee.energia.metal.dataprovider.BakeGoodRepository;
import ee.energia.metal.dataprovider.PurchaseRepository;
import ee.energia.metal.entity.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceTest {
    @Mock
    PurchaseRepository purchaseRepository;
    @Mock
    BakeGoodRepository bakeGoodRepository;
    @Captor
    ArgumentCaptor<Purchase> captor;
    @InjectMocks
    PurchaseService purchaseService;

    @Test
    void testCreatePurchase() {
        BakeGood bakeGood = new BakeGood();
        bakeGood.setPrice("4.0");
        bakeGood.setAmount(2L);
        when(bakeGoodRepository.findByCode(anyString())).thenReturn(Optional.of(bakeGood));
        when(purchaseRepository.save(captor.capture())).thenReturn(new Purchase());
        purchaseService.createPurchase("B, W, G");
        Purchase result = captor.getValue();
        assertEquals(new BigDecimal("12.0"), result.getPrice());
        assertEquals(3, result.getBakeGoods().size());
    }

    @Test
    void testCreatePurchaseErrorNotEnoughStock() {
        BakeGood bakeGood = new BakeGood();
        bakeGood.setPrice("4.0");
        bakeGood.setAmount(0L);
        when(bakeGoodRepository.findByCode(anyString())).thenReturn(Optional.of(bakeGood));

        assertThrows(NotEnoughStockException.class, () -> purchaseService.createPurchase("B, W, G"));
        verify(purchaseRepository, never()).save(any());
    }

    @Test
    void testCreatePurchaseErrorNoSuchGoodException() {
        BakeGood bakeGood = new BakeGood();
        bakeGood.setPrice("4.0");
        bakeGood.setAmount(0L);
        when(bakeGoodRepository.findByCode(anyString())).thenReturn(Optional.empty());

        assertThrows(NoSuchBakeGoodException.class, () -> purchaseService.createPurchase("B, W, G"));
        verify(purchaseRepository, never()).save(any());
    }

    @Test
    void testPay() {
        Purchase purchase = new Purchase();
        purchase.setPrice(new BigDecimal("2"));
        BakeGood bakeGood = new BakeGood();
        bakeGood.setAmount(1L);
        purchase.setBakeGoods(List.of(bakeGood));
        when(purchaseRepository.findById(1L)).thenReturn(Optional.of(purchase));
        when(purchaseRepository.save(captor.capture())).thenReturn(new Purchase());
        purchaseService.pay(1L, "2");
        assertEquals(BigDecimal.ZERO, captor.getValue().getChange());
    }


    @Test
    void testPayErrorPurchaseAlreadyCompleted() {
        Purchase purchase = new Purchase();
        purchase.setStatus(PurchaseStatus.COMPLETED);

        when(purchaseRepository.findById(1L)).thenReturn(Optional.of(purchase));

        assertThrows(PurchaseIsAlreadyCompletedException.class, () -> purchaseService.pay(1L, "2"));
        verify(purchaseRepository, never()).save(any());
    }

    @Test
    void testPayErrorPInsufficientMoneyException() {
        Purchase purchase = new Purchase();
        purchase.setPrice(new BigDecimal("3"));

        when(purchaseRepository.findById(1L)).thenReturn(Optional.of(purchase));

        assertThrows(InsufficientMoneyException.class, () -> purchaseService.pay(1L, "2"));
        verify(purchaseRepository, never()).save(any());
    }

    @Test
    void testGetIncompletePurchases() {
        List<Purchase> purchases = List.of(new Purchase());
        when(purchaseRepository.findAllByStatus(any())).thenReturn(purchases);

        Collection<Purchase> result = purchaseService.getIncompletePurchases();
        assertSame(purchases, result);
    }
}
