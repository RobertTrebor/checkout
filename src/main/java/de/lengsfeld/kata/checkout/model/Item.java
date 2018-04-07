package de.lengsfeld.kata.checkout.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Item {

    @Getter
    @Setter
    private String itemSku;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private BigDecimal standardPrice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return Objects.equals(itemSku, item.itemSku) &&
                Objects.equals(name, item.name) &&
                Objects.equals(standardPrice, item.standardPrice);
    }

    @Override
    public int hashCode() {

        return Objects.hash(itemSku, name, standardPrice);
    }
}
