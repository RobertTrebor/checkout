package de.lengsfeld.kata.checkout.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class SpecialDeal {

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
    private BigDecimal specialPrice;

}
