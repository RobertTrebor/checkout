package de.lengsfeld.kata.checkout.service;

import de.lengsfeld.kata.checkout.model.Purchase;
import de.lengsfeld.kata.checkout.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("purchaseService")
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Override
    public Purchase findById(Long id) {
        return purchaseRepository.findPurchaseById(id);
    }


}
