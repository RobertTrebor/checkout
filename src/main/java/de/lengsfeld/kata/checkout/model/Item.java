package de.lengsfeld.kata.checkout.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Item {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Getter
    @Setter
    @Column
    private String itemSku;

    @Getter
    @Setter
    @Column
    private String name;

    @Getter
    @Setter
    @Column
    private BigDecimal standardPrice;

    public Item() {
    }

    public Item(String itemSku, BigDecimal standardPrice) {
        this.itemSku = itemSku;
        this.standardPrice = standardPrice;
    }

    public Item(String itemSku, String name, BigDecimal standardPrice) {
        this.itemSku = itemSku;
        this.name = name;
        this.standardPrice = standardPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        return itemSku.equals(item.itemSku);
    }

    @Override
    public int hashCode() {
        return itemSku.hashCode();
    }
}
