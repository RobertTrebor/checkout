package de.lengsfeld.kata.checkout.controller;

import de.lengsfeld.kata.checkout.model.Item;
import de.lengsfeld.kata.checkout.model.Purchase;
import de.lengsfeld.kata.checkout.model.PurchaseLine;
import de.lengsfeld.kata.checkout.model.SpecialDeal;
import de.lengsfeld.kata.checkout.service.CheckoutService;
import de.lengsfeld.kata.checkout.service.ItemService;
import de.lengsfeld.kata.checkout.service.PurchaseService;
import de.lengsfeld.kata.checkout.service.SpecialDealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RestApiController {

    @Autowired
    private SpecialDealService specialDealService;

    @Autowired
    private ItemService itemsService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private CheckoutService checkoutService;

    @RequestMapping(value = "/specials", method = RequestMethod.GET)
    public ResponseEntity<List<SpecialDeal>> listAllSpecials() {
        List<SpecialDeal> specialDeals = specialDealService.findAll();
        if (specialDeals.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(specialDeals, HttpStatus.OK);
    }

    @RequestMapping(value = "/specials/{id}", method = RequestMethod.GET)
    public ResponseEntity<SpecialDeal> getSpecial(@PathVariable("id") long id) {
        SpecialDeal specialDeal = specialDealService.findById(id);
        if (specialDeal == null) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(specialDeal, HttpStatus.OK);
    }

    @RequestMapping(value = "/items/{id}", method = RequestMethod.GET)
    public ResponseEntity<Item> getItem(@PathVariable("id") long id) {
        Item item = itemsService.findById(id);
        if (item == null) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @RequestMapping(value = "/listitems", method = RequestMethod.GET)
    public ResponseEntity<List<Item>> listAllItems() {
        List<Item> items = itemsService.findAll();
        if (items.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @RequestMapping(value = "/scan/{id}", method = RequestMethod.GET)
    public ResponseEntity<Map<Item, Integer>> scanItem(@PathVariable("id") long id){
        Item item = itemsService.findById(id);
        Map<Item, Integer> scannedItems = checkoutService.scanItem(item);
        return new ResponseEntity<>(scannedItems, HttpStatus.OK);
    }

    @RequestMapping(value = "/purchase/{id}", method = RequestMethod.GET)
    public ResponseEntity<Purchase> getPurchase(@PathVariable("id") long id) {
        Purchase purchase = purchaseService.findById(id);
        return new ResponseEntity<>(purchase, HttpStatus.OK);
    }

    @RequestMapping(value = "/done", method = RequestMethod.GET)
    public ResponseEntity<Purchase> completePurchase() {
        Purchase purchase = checkoutService.purchaseCompleted();
        return new ResponseEntity<>(purchase, HttpStatus.OK);
    }

    @RequestMapping(value = "/lines", method = RequestMethod.GET)
    public ResponseEntity<List<PurchaseLine>> getPurchaseLines() {
        Purchase purchase = checkoutService.purchaseCompleted();
        List<PurchaseLine> purchaseLines = purchase.getPurchaseLines();
        return new ResponseEntity<>(purchaseLines, HttpStatus.OK);
    }

    @RequestMapping(value = "/total", method = RequestMethod.GET)
    public ResponseEntity<BigDecimal> getTotalAmount() {
        Purchase purchase = checkoutService.purchaseCompleted();
        List<PurchaseLine> purchaseLines = purchase.getPurchaseLines();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (PurchaseLine purchaseLine : purchaseLines) {
            totalAmount = totalAmount.add(purchaseLine.getApplicablePrice());
        }
        return new ResponseEntity<>(totalAmount, HttpStatus.OK);
    }



}