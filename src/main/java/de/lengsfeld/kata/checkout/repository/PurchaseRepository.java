package de.lengsfeld.kata.checkout.repository;

import de.lengsfeld.kata.checkout.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    Purchase findPurchaseById(long id);

}