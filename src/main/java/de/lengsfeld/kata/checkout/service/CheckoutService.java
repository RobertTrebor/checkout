package de.lengsfeld.kata.checkout.service;

import de.lengsfeld.kata.checkout.model.Item;
import de.lengsfeld.kata.checkout.model.Purchase;
import de.lengsfeld.kata.checkout.model.PurchaseLine;
import de.lengsfeld.kata.checkout.model.SpecialDeal;
import de.lengsfeld.kata.checkout.repository.ItemRepository;
import de.lengsfeld.kata.checkout.repository.SpecialDealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service("checkoutService")
public class CheckoutService {


    @Autowired
    private SpecialDealRepository specialDealRepository;

    @Autowired
    private ItemRepository itemRepository;

    private Map<Item, Integer> scannedItems = new HashMap<>();

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
            purchaseLines.addAll(getPurchaseLine(scannedItems, item));
        }
        Purchase purchase = new Purchase();
        purchase.setPurchaseLines(purchaseLines);
        return purchase;
    }

    protected List<PurchaseLine> getPurchaseLine(Map<Item, Integer> scannedItems, Item item) {

        List<PurchaseLine> purchaseLines = new ArrayList<>();
        PurchaseLine purchaseLine = new PurchaseLine();
        purchaseLine.setItem(item);
        int quantity = scannedItems.get(item);
        purchaseLine.setItem(item);
        SpecialDeal specialDeal = specialDealRepository.findSpecialDealByItem(item);
        if (specialDeal == null) {
            purchaseLine.setQuantity(quantity);
            purchaseLine.setApplicablePrice(item.getStandardPrice().multiply(BigDecimal.valueOf(quantity)));
            purchaseLines.add(purchaseLine);
        } else {
            int numberOfTimesDiscountApplied = getNumberOfTimesDiscountApplied(quantity, specialDeal.getQuantity());
            if (numberOfTimesDiscountApplied > 0) {
                purchaseLine.setQuantity(numberOfTimesDiscountApplied);
                purchaseLine.setApplicablePrice(specialDeal.getSpecialPrice());
                purchaseLines.add(purchaseLine);
            }
            int numberOfRemainingItems = quantity - specialDeal.getQuantity() * numberOfTimesDiscountApplied;
            if (numberOfRemainingItems > 0) {
                PurchaseLine additionalPurchaseLine = new PurchaseLine();
                additionalPurchaseLine.setItem(item);
                additionalPurchaseLine.setQuantity(numberOfRemainingItems);
                additionalPurchaseLine.setApplicablePrice(item.getStandardPrice().multiply(BigDecimal.valueOf(numberOfRemainingItems)));
                purchaseLines.add(additionalPurchaseLine);
            }
        }
        return purchaseLines;
    }

    private int getNumberOfTimesDiscountApplied(int quantity, int quantityRequiredForSpecial) {
        List<PurchaseLine> purchaseLines = new ArrayList<>();
        Float numberOfTimesDiscountApplied = quantity / Float.valueOf(quantityRequiredForSpecial);
        return numberOfTimesDiscountApplied.intValue();
    }
}