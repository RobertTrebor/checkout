package de.lengsfeld.kata.checkout.service;

import de.lengsfeld.kata.checkout.model.Item;
import de.lengsfeld.kata.checkout.model.SpecialDeal;
import de.lengsfeld.kata.checkout.repository.SpecialDealsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("specialDealsService")
public class SpecialDealsServiceImpl implements SpecialDealsService {

    @Autowired
    private SpecialDealsRepository specialDealsRepository;

    @Override
    public SpecialDeal findById(Long id) {
        return specialDealsRepository.findSpecialDealById(id);
    }

    @Override
    public SpecialDeal findByItem(Item item) {
        return specialDealsRepository.findSpecialDealByItem(item);

    }
}
