package ee.energia.metal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static ee.energia.metal.entity.PurchaseStatus.COMPLETED;
import static ee.energia.metal.entity.PurchaseStatus.INITIATED;

@Entity
@Data
public class Purchase implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Getter
    @ManyToMany(fetch = FetchType.EAGER)
    private List<BakeGood> bakeGoods = new ArrayList<>();
    private BigDecimal price;
    private String currency = "$";
    @Enumerated(EnumType.STRING)
    private PurchaseStatus status;
    private BigDecimal change;

    public Purchase() {
        status = INITIATED;
    }

    public void addBakeGood(BakeGood bakeGood) {
        bakeGoods.add(bakeGood);
    }

    @JsonIgnore
    public boolean isCompleted() {
        return status == COMPLETED;
    }

    public void complete() {
        status = COMPLETED;
    }

    public void calculateChange(BigDecimal paid) {
        change = paid.subtract(price);
    }
}
