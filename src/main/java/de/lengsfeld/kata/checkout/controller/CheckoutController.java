package de.lengsfeld.kata.checkout.controller;

import de.lengsfeld.kata.checkout.form.CheckoutForm;
import de.lengsfeld.kata.checkout.model.Item;
import de.lengsfeld.kata.checkout.model.Purchase;
import de.lengsfeld.kata.checkout.model.PurchaseLine;
import de.lengsfeld.kata.checkout.model.SpecialDeal;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


public class CheckoutController {

    @Inject
    private CheckoutForm checkoutForm;

    @Inject
    private SpecialDealsProducer specialDealsProducer;


    @PostConstruct
    public void init() {
        //specialDeals = specialDealsProducer.getSpecialDeals();
    }

    public void scanItem(Item item) {
        Map<Item, Integer> scannedItems = checkoutForm.getScannedItems();
        if (scannedItems.containsKey(item)) {
            Integer quantity = scannedItems.get(item);
            scannedItems.put(item, quantity + 1);
        } else {
            scannedItems.put(item, 1);
        }
    }

    public Purchase purchaseCompleted() {
        Map<Item, Integer> scannedItems = checkoutForm.getScannedItems();
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
        SpecialDeal specialDeal = specialDealsProducer.getSpecialDeal(item);
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
