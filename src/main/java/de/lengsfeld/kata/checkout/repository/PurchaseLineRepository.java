package de.lengsfeld.kata.checkout.repository;

import de.lengsfeld.kata.checkout.model.PurchaseLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseLineRepository extends JpaRepository<PurchaseLine, Long> {

    PurchaseLine findPurchaseLineById(long id);

}