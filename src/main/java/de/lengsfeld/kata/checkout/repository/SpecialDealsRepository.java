package de.lengsfeld.kata.checkout.repository;

import de.lengsfeld.kata.checkout.model.Item;
import de.lengsfeld.kata.checkout.model.SpecialDeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialDealsRepository extends JpaRepository<SpecialDeal, Long> {

    SpecialDeal findSpecialDealByItem(Item item);

    SpecialDeal findSpecialDealById(Long id);

}
