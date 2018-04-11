package de.lengsfeld.kata.checkout.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@EqualsAndHashCode
@Entity
public class PurchaseLine {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Getter
    @Setter
    @OneToOne
    private Item item;

    @Getter
    @Setter
    @Column
    private int quantity;

    @Getter
    @Setter
    @Column
    private BigDecimal applicablePrice;


    public BigDecimal getTotalAmount() {
        return BigDecimal.valueOf(quantity).multiply(applicablePrice);
    }

    @Override
    public String toString() {
        return "PurchaseLine{" +
                "id=" + id +
                ", item=" + item +
                ", quantity=" + quantity +
                ", applicablePrice=" + applicablePrice +
                '}';
    }
}
