package de.lengsfeld.kata.checkout.controller;

import de.lengsfeld.kata.checkout.model.Item;
import de.lengsfeld.kata.checkout.model.SpecialDeal;

import java.util.ArrayList;
import java.util.List;

public class SpecialDealsProducer {

    public List<SpecialDeal> getSpecialDeals() {
        return new ArrayList<>();
    }

    public SpecialDeal getSpecialDeal(Item item) {
        return new SpecialDeal();
    }

}
