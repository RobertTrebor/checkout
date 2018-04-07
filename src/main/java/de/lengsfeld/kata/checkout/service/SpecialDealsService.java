package de.lengsfeld.kata.checkout.service;

import de.lengsfeld.kata.checkout.model.Item;
import de.lengsfeld.kata.checkout.model.SpecialDeal;

public interface SpecialDealsService {

    SpecialDeal findById(Long id);

    SpecialDeal findByItem(Item item);

}
