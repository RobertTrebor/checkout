package de.lengsfeld.kata.checkout.service;

import de.lengsfeld.kata.checkout.model.Item;
import de.lengsfeld.kata.checkout.model.Purchase;
import de.lengsfeld.kata.checkout.model.PurchaseLine;
import de.lengsfeld.kata.checkout.model.SpecialDeal;
import de.lengsfeld.kata.checkout.repository.PurchaseLineRepository;
import de.lengsfeld.kata.checkout.repository.PurchaseRepository;
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
    private PurchaseRepository purchaseRepository;

    @Autowired
    private PurchaseLineRepository purchaseLineRepository;

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
            purchaseLines.addAll(getPurchaseLines(scannedItems, item));

        }
        Purchase purchase = new Purchase();
        purchase.setPurchaseLines(purchaseLines);
        purchase.setTotalAmount(calculateTotalAmount(purchase));
        purchaseRepository.save(purchase);
        scannedItems = new HashMap<>();
        return purchase;
    }


    protected List<PurchaseLine> getPurchaseLines(Map<Item, Integer> scannedItems, Item item) {
        List<PurchaseLine> purchaseLines = new ArrayList<>();
        int quantity = scannedItems.get(item);
        SpecialDeal specialDeal = specialDealRepository.findSpecialDealByItem(item);
        if (specialDeal != null) {
            int numberOfTimesDiscountApplied = getNumberOfTimesDiscountApplied(quantity, specialDeal.getQuantity());
            if (numberOfTimesDiscountApplied > 0) {
                purchaseLines.add(getPurchaseLine(item, numberOfTimesDiscountApplied, specialDeal.getSpecialPrice()));
            }
            quantity = quantity - specialDeal.getQuantity() * numberOfTimesDiscountApplied;
        }
        if (quantity > 0) {
            purchaseLines.add(getPurchaseLine(item, quantity, item.getStandardPrice()));
        }
        return purchaseLines;
    }

    private PurchaseLine getPurchaseLine(Item item, int quantity, BigDecimal price) {
        PurchaseLine purchaseLine = new PurchaseLine();
        purchaseLine.setItem(item);
        purchaseLine.setQuantity(quantity);
        purchaseLine.setApplicablePrice(price);
        purchaseLineRepository.save(purchaseLine);
        return purchaseLine;
    }

    private int getNumberOfTimesDiscountApplied(int quantity, int quantityRequiredForSpecial) {
        Float numberOfTimesDiscountApplied = quantity / Float.valueOf(quantityRequiredForSpecial);
        return numberOfTimesDiscountApplied.intValue();
    }

    private BigDecimal calculateTotalAmount(Purchase purchase) {
        List<PurchaseLine> purchaseLines = purchase.getPurchaseLines();
        BigDecimal totalAmountForPurchase = BigDecimal.ZERO;
        for (PurchaseLine purchaseLine : purchaseLines) {
            totalAmountForPurchase = totalAmountForPurchase.add(purchaseLine.getTotalAmount());
        }
        return totalAmountForPurchase;
    }

}