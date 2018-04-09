package de.lengsfeld.kata.checkout.repository;

import de.lengsfeld.kata.checkout.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Item findItemById(long id);

}