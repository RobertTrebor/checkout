package de.lengsfeld.kata.checkout.service;

import de.lengsfeld.kata.checkout.model.Item;
import de.lengsfeld.kata.checkout.model.Purchase;
import de.lengsfeld.kata.checkout.model.PurchaseLine;
import de.lengsfeld.kata.checkout.model.SpecialDeal;
import de.lengsfeld.kata.checkout.repository.ItemRepository;
import de.lengsfeld.kata.checkout.repository.SpecialDealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service("checkoutService")
public class CheckoutService {


    @Autowired
    private SpecialDealRepository specialDealRepository;

    @Autowired
    private ItemRepository itemRepository;

    private Map<Item, Integer> scannedItems;

    public Map<Item, Integer> scanItem(Item item) {
        if (scannedItems.containsKey(item)) {
            Integer quantity = scannedItems.get(item);
            scannedItems.put(item, quantity + 1);
        } else {
            scannedItems.put(item, 1);
        }
        return scannedItems;
    }

    public Purchase purchaseCompleted() {
        Collection<Item> items = scannedItems.keySet();
        List<PurchaseLine> purchaseLines = new ArrayList<>();
        for (Item item : items) {
            purchaseLines.add(getPurchaseLine(scannedItems, item));
        }
        Purchase purchase = new Purchase();
        purchase.setPurchaseLines(purchaseLines);
        return purchase;
    }

    protected PurchaseLine getPurchaseLine(Map<Item, Integer> scannedItems, Item item) {
        PurchaseLine purchaseLine = new PurchaseLine();
        purchaseLine.setItem(item);
        int quantity = scannedItems.get(item);
        purchaseLine.setQuantity(quantity);
        SpecialDeal specialDeal = specialDealRepository.findSpecialDealByItem(item);
        if (specialDeal != null) {
            if (specialDeal.getQuantity() >= quantity) {
                purchaseLine.setApplicablePrice(specialDeal.getSpecialPrice());
                return purchaseLine;
            }
        }
        purchaseLine.setApplicablePrice(item.getStandardPrice());
        return purchaseLine;
    }
}