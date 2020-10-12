package ee.energia.metal.endpoint;

import ee.energia.metal.entity.BakeSaleException;
import ee.energia.metal.entity.Purchase;
import ee.energia.metal.service.PurchaseService;
import org.springframework.boot.CommandLineRunner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicReference;


public class PurchaseConsoleController implements CommandLineRunner {
    public static final String ITEMS_TO_PURCHASE = "Items to Purchase > ";
    private final BufferedReader br;
    private final PurchaseService purchaseService;
    private final AtomicReference<Purchase> purchaseAtomicReference = new AtomicReference<>();

    public PurchaseConsoleController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
        this.br = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.print(ITEMS_TO_PURCHASE);
        while (true) {
            try {
                String line = br.readLine();
                if (line == null || isExitCommand(line)) {
                    break;
                }
                if (!line.isEmpty()) {
                    handlePurchaseInput(line);
                }
            } catch (BakeSaleException e) {
                System.out.println(e.getMessage());
                refreshPurchase();
            }
        }
    }

    private boolean isExitCommand(String line) {
        return line.toLowerCase().startsWith("exit");
    }

    private void handlePurchaseInput(String line) {
        var savedPurchase = purchaseAtomicReference.get();
        if (savedPurchase != null) {
            receiveMoney(line, savedPurchase);
            refreshPurchase();
        } else {
            createPurchase(line);
        }
    }

    private void createPurchase(String line) {
        System.out.print("Total > ");
        var purchase = purchaseService.createPurchase(line);
        purchaseAtomicReference.set(purchase);
        System.out.println(" " + purchase.getCurrency() + purchase.getPrice());
        System.out.print("Amount Paid > ");
    }

    private void receiveMoney(String line, Purchase savedPurchase) {
        System.out.print("Change > ");
        var purchase = purchaseService.pay(savedPurchase.getId(), line);
        System.out.println(" " + purchase.getCurrency() + purchase.getChange());
        System.out.println("Thank you!");
    }

    private void refreshPurchase() {
        purchaseAtomicReference.set(null);
        System.out.print(ITEMS_TO_PURCHASE);
    }
}