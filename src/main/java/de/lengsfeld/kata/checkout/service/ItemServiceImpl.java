package de.lengsfeld.kata.checkout.service;

import de.lengsfeld.kata.checkout.model.Item;
import de.lengsfeld.kata.checkout.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("itemService")
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public Item findById(Long id) {
        return itemRepository.findItemById(id);
    }

    @Override
    public Item findByItem(Item item) {
        return item;
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<>();
    }

}
