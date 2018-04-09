package de.lengsfeld.kata.checkout.service;

import de.lengsfeld.kata.checkout.model.Item;
import de.lengsfeld.kata.checkout.model.SpecialDeal;
import de.lengsfeld.kata.checkout.repository.SpecialDealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("specialDealService")
public class SpecialDealServiceImpl implements SpecialDealService {

    @Autowired
    private SpecialDealRepository specialDealRepository;

    @Override
    public SpecialDeal findById(Long id) {
        return specialDealRepository.findSpecialDealById(id);
    }

    @Override
    public SpecialDeal findByItem(Item item) {
        return specialDealRepository.findSpecialDealByItem(item);
    }

    @Override
    public List<SpecialDeal> findAll() {
        return specialDealRepository.findAll();
    }
}
