package de.lengsfeld.kata.checkout.service;

import de.lengsfeld.kata.checkout.model.Item;
import de.lengsfeld.kata.checkout.model.Purchase;
import de.lengsfeld.kata.checkout.model.PurchaseLine;
import de.lengsfeld.kata.checkout.model.SpecialDeal;
import de.lengsfeld.kata.checkout.repository.SpecialDealRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutServiceTest {

    private static final String SKU_SPECIALS = "1SKU";
    private static final BigDecimal STANDARD_PRICE = BigDecimal.TEN;
    private static final BigDecimal SPECIAL_PRICE = BigDecimal.valueOf(25);
    private static final Integer QUANTITY_REQUIRED = 3;

    private static final String SKU_NO_SPECIALS = "2SKU";
    private static final BigDecimal NO_SPECIALS_PRICE = BigDecimal.valueOf(20);

    @InjectMocks
    private CheckoutService checkoutService;

    @Mock
    private SpecialDealRepository specialDealRepository;

    private SpecialDeal specialDeal;

    private Item item;
    private Item itemWithSpecialDeal;


    @Before
    public void setUp() {
        itemWithSpecialDeal = new Item(SKU_SPECIALS, STANDARD_PRICE);
        item = new Item(SKU_NO_SPECIALS, NO_SPECIALS_PRICE);
        specialDeal = new SpecialDeal(itemWithSpecialDeal, QUANTITY_REQUIRED, SPECIAL_PRICE);
        when(specialDealRepository.findSpecialDealByItem(item)).thenReturn(null);
        when(specialDealRepository.findSpecialDealByItem(itemWithSpecialDeal)).thenReturn(specialDeal);
    }


    @Test
    public void purchaseCompleted_verifyTotal() {
        when(specialDealRepository.findSpecialDealByItem(item)).thenReturn(null);
        when(specialDealRepository.findSpecialDealByItem(itemWithSpecialDeal)).thenReturn(specialDeal);
        int NUMBER_ITEMS_SPECIAL_DEAL = 4;
        int NUMBER_ITEMS_STANDARD = 6;
        scanSomeItems(NUMBER_ITEMS_SPECIAL_DEAL, NUMBER_ITEMS_STANDARD);
        Purchase purchase = checkoutService.purchaseCompleted();
        assertEquals(BigDecimal.valueOf(25 + 10 + 120), purchase.getTotalAmount());
    }

    @Test
    public void purchaseCompleted_itemWithAndWithoutDiscounts() {
        when(specialDealRepository.findSpecialDealByItem(item)).thenReturn(null);
        when(specialDealRepository.findSpecialDealByItem(itemWithSpecialDeal)).thenReturn(specialDeal);
        int NUMBER_ITEMS_SPECIAL_DEAL = 4;
        int NUMBER_ITEMS_STANDARD = 6;
        boolean success = testPurchaseCompleted(NUMBER_ITEMS_SPECIAL_DEAL, NUMBER_ITEMS_STANDARD);
        assertTrue(success);
    }

    private boolean testPurchaseCompleted(int NUMBER_ITEMS_SPECIAL_DEAL,
                                          int NUMBER_ITEMS_STANDARD) {
        scanSomeItems(NUMBER_ITEMS_SPECIAL_DEAL, NUMBER_ITEMS_STANDARD);
        Purchase purchase = checkoutService.purchaseCompleted();
        //assertEquals(NUMBER_DISTICT_ITEMS, purchase.getPurchaseLines().size());

        List<PurchaseLine> purchaseLines = purchase.getPurchaseLines();
        System.out.println(purchaseLines.size());
        int totalNumberOfItems = 0;
        int numberOfItemsWithSpecialDeal = 0;
        int numberOfItemsStandard = 0;
        BigDecimal totalCheckoutAmount = BigDecimal.ZERO;
        for (PurchaseLine purchaseLine : purchaseLines) {
            if (purchaseLine.getItem().equals(item)) {
                numberOfItemsStandard += purchaseLine.getQuantity();
            }
            if (purchaseLine.getItem().equals(itemWithSpecialDeal)) {
                numberOfItemsWithSpecialDeal += purchaseLine.getQuantity();
            }

            totalCheckoutAmount = totalCheckoutAmount.add(purchaseLine.getApplicablePrice()
                    .multiply(BigDecimal.valueOf(purchaseLine.getQuantity())));
        }

        assertEquals(NUMBER_ITEMS_STANDARD, numberOfItemsStandard);
        return true;
    }


    private void scanSomeItems(int numberItemsWithSpecialDeal, int numberItemsWithoutSpecialDeal) {
        for (int i = 0; i < numberItemsWithSpecialDeal; i++) {
            checkoutService.scanItem(itemWithSpecialDeal);
        }
        for (int i = 0; i < numberItemsWithoutSpecialDeal; i++) {
            checkoutService.scanItem(item);
        }
    }

}