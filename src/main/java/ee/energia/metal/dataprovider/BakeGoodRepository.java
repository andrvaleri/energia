package ee.energia.metal.dataprovider;

import ee.energia.metal.entity.BakeGood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BakeGoodRepository extends JpaRepository<BakeGood, Long> {
    Optional<BakeGood> findByCode(String code);
}
