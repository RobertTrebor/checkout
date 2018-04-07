package de.lengsfeld.kata.checkout.form;

import de.lengsfeld.kata.checkout.model.Item;

import java.util.HashMap;
import java.util.Map;

public class CheckoutForm {


    private Map<Item, Integer> scannedItems;

    public Map<Item, Integer> getScannedItems() {
        if (scannedItems == null) {
            scannedItems = new HashMap<>();
        }
        return scannedItems;
    }

}
