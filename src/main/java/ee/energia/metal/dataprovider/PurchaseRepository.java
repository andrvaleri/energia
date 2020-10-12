package ee.energia.metal.dataprovider;

import ee.energia.metal.entity.Purchase;
import ee.energia.metal.entity.PurchaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findAllByStatus(PurchaseStatus initiated);
}
