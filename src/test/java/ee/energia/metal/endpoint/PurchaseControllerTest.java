package ee.energia.metal.endpoint;

import ee.energia.metal.entity.Purchase;
import ee.energia.metal.service.PurchaseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseControllerTest {
    @Mock
    PurchaseService service;
    @InjectMocks
    PurchaseController purchaseController;

    @Test
    void testGetAvailablePurchases() {
        Purchase purchase = new Purchase();
        List<Purchase> purchases = List.of(purchase);
        when(service.getIncompletePurchases()).thenReturn(purchases);

        CollectionModel<Purchase> result = purchaseController.getAvailablePurchases();
        assertEquals(1, result.getContent().size());
        assertTrue(result.getLinks().hasSize(2));
    }

    @Test
    void testGetAvailablePurchasesNoPurchases() {
        List<Purchase> purchases = List.of();
        when(service.getIncompletePurchases()).thenReturn(purchases);
        CollectionModel<Purchase> result = purchaseController.getAvailablePurchases();
        assertEquals(0, result.getContent().size());
        assertTrue(result.getLinks().hasSize(1));
    }

    @Test
    void testCreate() {
        Purchase purchase = new Purchase();
        when(service.createPurchase(anyString())).thenReturn(purchase);
        EntityModel<Purchase> result = purchaseController.create("bakeGoods");
        assertSame(purchase, result.getContent());
        assertTrue(result.getLinks().hasSize(1));
    }

    @Test
    void testPay() {
        Purchase purchase = new Purchase();
        when(service.pay(anyLong(), anyString())).thenReturn(purchase);
        EntityModel<Purchase> result = purchaseController.pay(1L, "amountPaid");
        assertSame(purchase, result.getContent());
        assertTrue(result.getLinks().hasSize(1));
    }
}