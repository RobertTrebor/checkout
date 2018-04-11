package de.lengsfeld.kata.checkout.service;

import de.lengsfeld.kata.checkout.model.PurchaseLine;

public interface PurchaseLineService {

    PurchaseLine findById(Long id);

}
