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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("checkoutService")
public class CheckoutService {


    @Autowired
    private SpecialDealRepository specialDealRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private PurchaseLineRepository purchaseLineRepository;

    private Map<Item, Integer> scannedItems;

    public Map<Item, Integer> scanItem(Item item) {
        if (scannedItems == null) {
            scannedItems = new HashMap<>();
        }
        if (item != null) {
            int quantity = 1;
            if (scannedItems.containsKey(item)) {
                quantity += scannedItems.get(item);
            }
            scannedItems.put(item, quantity);
        }
        return scannedItems;
    }

    public Purchase purchaseCompleted() {
        Purchase purchase = new Purchase();
        if (scannedItems != null && !scannedItems.isEmpty()) {
            List<PurchaseLine> purchaseLines = new ArrayList<>();
            for (Map.Entry<Item, Integer> entry : scannedItems.entrySet()) {
                purchaseLines.addAll(getPurchaseLines(entry.getKey(), entry.getValue()));
            }
            purchase.setPurchaseLines(purchaseLines);
            purchase.setTotalAmount(calculateTotalAmount(purchaseLines));
            purchaseRepository.save(purchase);
            scannedItems = null;
        }
        return purchase;
    }

    protected List<PurchaseLine> getPurchaseLines(Item item, int quantity) {
        List<PurchaseLine> purchaseLines = new ArrayList<>();
        SpecialDeal specialDeal = specialDealRepository.findSpecialDealByItem(item);
        if (specialDeal != null) {
            int numberOfTimesDiscountApplied = getNumberOfTimesDiscountApplied(quantity, specialDeal.getQuantity());
            if (numberOfTimesDiscountApplied > 0) {
                purchaseLines.add(getPurchaseLine(item, numberOfTimesDiscountApplied, specialDeal.getSpecialPrice()));
            }
            quantity = quantity - (specialDeal.getQuantity() * numberOfTimesDiscountApplied);
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

    private BigDecimal calculateTotalAmount(List<PurchaseLine> purchaseLines) {
        BigDecimal totalAmountForPurchase = BigDecimal.ZERO;
        for (PurchaseLine purchaseLine : purchaseLines) {
            totalAmountForPurchase = totalAmountForPurchase.add(purchaseLine.getTotalAmount());
        }
        return totalAmountForPurchase;
    }

}