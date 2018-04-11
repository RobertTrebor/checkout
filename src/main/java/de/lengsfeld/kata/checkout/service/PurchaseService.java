package de.lengsfeld.kata.checkout.service;

import de.lengsfeld.kata.checkout.model.Purchase;

public interface PurchaseService {

    Purchase findById(Long id);

}
