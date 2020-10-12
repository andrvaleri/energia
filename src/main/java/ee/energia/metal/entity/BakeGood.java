package ee.energia.metal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

import static ee.energia.metal.entity.MoneyUtility.transformMoney;

@Data
@Entity
public class BakeGood implements Serializable {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;
    private String name;
    @JsonIgnore
    private String code;
    @JsonIgnore
    private BigDecimal price;
    @JsonIgnore
    private Long amount;

    public void setPrice(String price) {
        this.price = transformMoney(price);
    }

    public BakeGood reduceAmount() {
        if (amount != null && amount > 0) {
            amount -= 1;
        }
        return this;
    }
}
