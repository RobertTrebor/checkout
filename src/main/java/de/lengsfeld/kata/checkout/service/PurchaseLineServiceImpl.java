package de.lengsfeld.kata.checkout.service;

import de.lengsfeld.kata.checkout.model.PurchaseLine;
import de.lengsfeld.kata.checkout.repository.PurchaseLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("purchaseLineService")
public class PurchaseLineServiceImpl implements PurchaseLineService {

    @Autowired
    private PurchaseLineRepository purchaseLineRepository;

    @Override
    public PurchaseLine findById(Long id) {
        return purchaseLineRepository.findPurchaseLineById(id);
    }


}
