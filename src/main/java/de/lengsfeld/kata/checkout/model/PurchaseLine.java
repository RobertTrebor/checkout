package de.lengsfeld.kata.checkout.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class PurchaseLine {

    @Getter
    @Setter
    private Item item;

    @Getter
    @Setter
    private int quantity;

    @Getter
    @Setter
    private BigDecimal applicablePrice;

}
