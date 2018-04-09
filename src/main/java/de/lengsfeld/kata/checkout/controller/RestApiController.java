package de.lengsfeld.kata.checkout.controller;

import de.lengsfeld.kata.checkout.model.Item;
import de.lengsfeld.kata.checkout.model.SpecialDeal;
import de.lengsfeld.kata.checkout.service.ItemService;
import de.lengsfeld.kata.checkout.service.SpecialDealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RestApiController {

    @Autowired
    private SpecialDealService specialDealService;

    @Autowired
    private ItemService itemsService;

    //@Autowired
    //private CheckoutService checkoutService;

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

    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public ResponseEntity<List<Item>> listAllItems() {
        List<Item> items = itemsService.findAll();
        if (items.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
/*
    @RequestMapping(value = "/scan/{id}", method = RequestMethod.GET)
    public ResponseEntity<Map<Item, Integer>> scanItem(@PathVariable("id") long id){
        Item item = itemsService.findById(id);
        Map<Item, Integer> scannedItems = checkoutService.scanItem(item);
        return new ResponseEntity<>(scannedItems, HttpStatus.OK);
    }
*/
}