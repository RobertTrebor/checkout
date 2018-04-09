package de.lengsfeld.kata.checkout.service;

import de.lengsfeld.kata.checkout.model.Item;

import java.util.List;

public interface ItemService {

    Item findById(Long id);

    Item findByItem(Item item);

    List<Item> findAll();

}
