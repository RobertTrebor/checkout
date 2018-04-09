package de.lengsfeld.kata.checkout.service;

import de.lengsfeld.kata.checkout.model.Item;
import de.lengsfeld.kata.checkout.model.SpecialDeal;

import java.util.List;

public interface SpecialDealService {

    SpecialDeal findById(Long id);

    SpecialDeal findByItem(Item item);

    List<SpecialDeal> findAll();

}
