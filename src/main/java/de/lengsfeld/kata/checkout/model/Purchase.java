package de.lengsfeld.kata.checkout.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode
@Entity
public class Purchase {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Getter
    @Setter
    @OneToMany
    private List<PurchaseLine> purchaseLines;

    @Getter
    @Setter
    @Column
    private BigDecimal totalAmount;


}
