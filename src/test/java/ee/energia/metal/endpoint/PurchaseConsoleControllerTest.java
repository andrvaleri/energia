package ee.energia.metal.endpoint;

import ee.energia.metal.entity.*;
import ee.energia.metal.service.PurchaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurchaseConsoleControllerTest {
    @Mock
    PurchaseService purchaseService;
    PurchaseConsoleController purchaseConsoleController;

    @BeforeEach
    void setUp() {
        purchaseConsoleController = new PurchaseConsoleController(purchaseService);
    }

    @Test
    @StdIo({"B,C,W", "$4"})
    void testRunHappyFlow(StdOut out) throws Exception {
        Purchase purchase = new Purchase();
        purchase.setPrice(new BigDecimal("1"));
        long id = 2L;
        purchase.setId(id);
        Purchase finalPurchase = new Purchase();
        finalPurchase.setChange(new BigDecimal("2"));
        when(purchaseService.createPurchase(anyString())).thenReturn(purchase);
        when(purchaseService.pay(id, "$4")).thenReturn(finalPurchase);
        purchaseConsoleController.run();
        assertEquals("Items to Purchase > Total >  $1", out.capturedLines()[0]);
        assertEquals("Amount Paid > Change >  $2", out.capturedLines()[1]);
        assertEquals("Thank you!", out.capturedLines()[2]);
        assertEquals("Items to Purchase > ", out.capturedLines()[3]);
    }

    @Test
    @StdIo({"B,C,W", "$4", "B,C,W", "B,C,W"})
    void testRunErrors(StdOut out) throws Exception {
        Purchase purchase = new Purchase();
        purchase.setPrice(new BigDecimal("1"));
        long id = 2L;
        purchase.setId(id);
        when(purchaseService.createPurchase(anyString()))
                .thenReturn(purchase)
                .thenThrow(new NoSuchBakeGoodException("B"))
                .thenThrow(new NotEnoughStockException());
        when(purchaseService.pay(id, "$4")).thenThrow(new InsufficientMoneyException());
        purchaseConsoleController.run();
        assertEquals("Items to Purchase > Total >  $1", out.capturedLines()[0]);
        assertEquals("Amount Paid > Change > Not Enough Money.", out.capturedLines()[1]);
        assertEquals("Items to Purchase > Total > Not Such BakeGood:B.", out.capturedLines()[2]);
        assertEquals("Items to Purchase > Total > Not Enough Stock.", out.capturedLines()[3]);
    }
}
