package de.lengsfeld.kata.checkout.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class SpecialDeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private long id;

    @Getter
    @Setter
    @Column(name = "ITEM")
    private Item item;

    @Getter
    @Setter
    @Column(name = "QUANTITY")
    private int quantity;

    @Getter
    @Setter
    @Column(name = "SPECIAL_PRICE")
    private BigDecimal specialPrice;

}
