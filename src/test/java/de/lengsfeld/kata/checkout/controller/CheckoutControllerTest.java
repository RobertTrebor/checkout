package de.lengsfeld.kata.checkout.controller;

import de.lengsfeld.kata.checkout.form.CheckoutForm;
import de.lengsfeld.kata.checkout.model.Item;
import de.lengsfeld.kata.checkout.model.Purchase;
import de.lengsfeld.kata.checkout.model.PurchaseLine;
import de.lengsfeld.kata.checkout.model.SpecialDeal;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutControllerTest {

    private static final BigDecimal SPECIAL_PRICE = BigDecimal.valueOf(25);
    private static final BigDecimal STANDARD_PRICE = BigDecimal.TEN;
    private static final BigDecimal NO_SPECIALS_PRICE = BigDecimal.valueOf(20);
    private static final String SKU_SPECIALS = "1SKU";
    private static final String SKU_NO_SPECIALS = "2SKU";

    @InjectMocks
    private CheckoutController checkoutController;

    @Spy
    private CheckoutForm checkoutForm;

    @Mock
    private SpecialDealsProducer specialDealsProducer;

    private SpecialDeal specialDeal;

    private Item item;
    private Item itemWithSpecialDeal;

    private Boolean purchaseLineContainsItemWithSpecialDeal;
    private Boolean specialDealAppliedAtLeastOnce;
    private Boolean standardPriceAppliedAtLeastOnce;


    @Before
    public void setUp() {
        itemWithSpecialDeal = new Item();
        itemWithSpecialDeal.setItemSku(SKU_SPECIALS);
        itemWithSpecialDeal.setStandardPrice(STANDARD_PRICE);

        specialDeal = new SpecialDeal();
        specialDeal.setItem(itemWithSpecialDeal);
        specialDeal.setQuantity(3);
        specialDeal.setSpecialPrice(SPECIAL_PRICE);

        item = new Item();
        item.setItemSku(SKU_NO_SPECIALS);
        item.setStandardPrice(NO_SPECIALS_PRICE);

        purchaseLineContainsItemWithSpecialDeal = false;
        specialDealAppliedAtLeastOnce = false;
        standardPriceAppliedAtLeastOnce = false;
    }

    @After
    public void tearDown() {
    }

    @Test
    public void scanItem() {
        checkoutController.scanItem(itemWithSpecialDeal);
    }

    @Test
    public void purchaseCompleted_One() {
        checkoutController.scanItem(itemWithSpecialDeal);
        Map<Item, Integer> scannedItems = checkoutForm.getScannedItems();
        assertNotNull(scannedItems);
    }

    @Test
    public void purchaseCompleted_DiscountsApplied() {
        when(specialDealsProducer.getSpecialDeal(itemWithSpecialDeal)).thenReturn(specialDeal);
        int NUMBER_ITEMS_SPECIAL_DEAL = 3;
        int NUMBER_ITEMS_STANDARD = 0;
        int NUMBER_DISTICT_ITEMS = 1;

        testPurchaseCompleted(NUMBER_ITEMS_SPECIAL_DEAL, NUMBER_ITEMS_STANDARD, NUMBER_DISTICT_ITEMS);

        assertTrue(purchaseLineContainsItemWithSpecialDeal);
        assertTrue(specialDealAppliedAtLeastOnce);
        assertFalse(standardPriceAppliedAtLeastOnce);
    }

    @Test
    public void purchaseCompleted_NoDiscountsApplied() {
        //when(specialDealsProducer.getSpecialDeal(itemWithSpecialDeal)).thenReturn(null);
        int NUMBER_ITEMS_SPECIAL_DEAL = 0;
        int NUMBER_ITEMS_STANDARD = 6;
        int NUMBER_DISTICT_ITEMS = 1;
        testPurchaseCompleted(NUMBER_ITEMS_SPECIAL_DEAL, NUMBER_ITEMS_STANDARD, NUMBER_DISTICT_ITEMS);

        assertFalse(purchaseLineContainsItemWithSpecialDeal);
        assertFalse(specialDealAppliedAtLeastOnce);
        assertTrue(standardPriceAppliedAtLeastOnce);
    }

    @Test
    public void purchaseCompleted_itemWithAndWithoutDiscounts() {
        when(specialDealsProducer.getSpecialDeal(item)).thenReturn(null);
        when(specialDealsProducer.getSpecialDeal(itemWithSpecialDeal)).thenReturn(specialDeal);
        int NUMBER_ITEMS_SPECIAL_DEAL = 3;
        int NUMBER_ITEMS_STANDARD = 6;
        int NUMBER_DISTICT_ITEMS = 2;
        testPurchaseCompleted(NUMBER_ITEMS_SPECIAL_DEAL, NUMBER_ITEMS_STANDARD, NUMBER_DISTICT_ITEMS);

        assertTrue(purchaseLineContainsItemWithSpecialDeal);
        assertTrue(specialDealAppliedAtLeastOnce);
        assertTrue(standardPriceAppliedAtLeastOnce);
    }

    private void testPurchaseCompleted(int NUMBER_ITEMS_SPECIAL_DEAL,
                                       int NUMBER_ITEMS_STANDARD,
                                       int NUMBER_DISTICT_ITEMS) {
        scanSomeItems(NUMBER_ITEMS_SPECIAL_DEAL, NUMBER_ITEMS_STANDARD);
        Purchase purchase = checkoutController.purchaseCompleted();
        assertEquals(NUMBER_DISTICT_ITEMS, purchase.getPurchaseLines().size());
        Map<Item, Integer> scannedItems = checkoutForm.getScannedItems();
        if (NUMBER_ITEMS_SPECIAL_DEAL > 0) {
            assertTrue(scannedItems.containsKey(itemWithSpecialDeal));
        }
        if (NUMBER_ITEMS_STANDARD > 0) {
            assertTrue(scannedItems.containsKey(item));
        }

        Collection<Item> items = scannedItems.keySet();
        assertEquals(NUMBER_DISTICT_ITEMS, items.size());
        int totalNumberOfItems = 0;
        for (Item i : items) {
            totalNumberOfItems += scannedItems.get(i);
            System.out.println(scannedItems.get(i));
        }
        assertEquals(NUMBER_ITEMS_SPECIAL_DEAL + NUMBER_ITEMS_STANDARD, totalNumberOfItems);

        List<PurchaseLine> purchaseLines = purchase.getPurchaseLines();
        assertEquals(NUMBER_DISTICT_ITEMS, purchaseLines.size());
        for (PurchaseLine purchaseLine : purchaseLines) {
            assertTrue(purchaseLine.getItem().equals(item) || purchaseLine.getItem().equals(itemWithSpecialDeal));
            specialDealAppliedAtLeastOnce = specialDealExistsAndApplies(purchaseLine);
            if (specialDealAppliedAtLeastOnce) {
                assertEquals(NUMBER_ITEMS_SPECIAL_DEAL, purchaseLine.getQuantity());
            } else {
                assertEquals(NUMBER_ITEMS_STANDARD, purchaseLine.getQuantity());
                assertEquals(NO_SPECIALS_PRICE.multiply(BigDecimal.valueOf(NUMBER_ITEMS_STANDARD)), purchaseLine.getApplicablePrice().multiply(BigDecimal.valueOf(purchaseLine.getQuantity())));
                standardPriceAppliedAtLeastOnce = true;
            }
        }
    }


    private boolean specialDealExistsAndApplies(PurchaseLine purchaseLine) {
        if (purchaseLine.getItem().getItemSku().equals(SKU_SPECIALS)) {
            purchaseLineContainsItemWithSpecialDeal = true;
            if (specialDeal.getQuantity() == purchaseLine.getQuantity()) {
                assertEquals(SPECIAL_PRICE, purchaseLine.getApplicablePrice());
                return true;
            }
        }
        return false;
    }

    @Test
    public void getPurchaseLine() {
        int numberItemsWithSpecialDeal = 3;
        int numberItemsWithoutSpecialDeal = 2;
        scanSomeItems(numberItemsWithSpecialDeal, numberItemsWithoutSpecialDeal);
        Purchase purchase = checkoutController.purchaseCompleted();
        assertEquals(2, purchase.getPurchaseLines().size());
        Map<Item, Integer> scannedItems = checkoutForm.getScannedItems();

        PurchaseLine purchaseLine = checkoutController.getPurchaseLine(scannedItems, item);
        assertEquals(2, purchaseLine.getQuantity());
        purchaseLine = checkoutController.getPurchaseLine(scannedItems, itemWithSpecialDeal);
        assertEquals(3, purchaseLine.getQuantity());
    }

    private void scanSomeItems(int numberItemsWithSpecialDeal, int numberItemsWithoutSpecialDeal) {
        for (int i = 0; i < numberItemsWithSpecialDeal; i++) {
            checkoutController.scanItem(itemWithSpecialDeal);
        }
        for (int i = 0; i < numberItemsWithoutSpecialDeal; i++) {
            checkoutController.scanItem(item);
        }
    }


}