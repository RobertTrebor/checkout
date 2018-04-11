package de.lengsfeld.kata.checkout;

import de.lengsfeld.kata.checkout.model.Item;
import de.lengsfeld.kata.checkout.model.Purchase;
import de.lengsfeld.kata.checkout.model.PurchaseLine;
import de.lengsfeld.kata.checkout.model.SpecialDeal;
import de.lengsfeld.kata.checkout.repository.ItemRepository;
import de.lengsfeld.kata.checkout.repository.SpecialDealRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CheckoutApplicationTests {

    private static final String SKU_SPECIALS = "1SKU";
    private static final BigDecimal STANDARD_PRICE = new BigDecimal(10);
    private static final BigDecimal SPECIAL_PRICE = new BigDecimal(25);
    private static final int QUANTITY_REQUIRED = 3;
    private static final String SKU_NO_SPECIALS = "2SKU";
    private static final BigDecimal NO_SPECIALS_PRICE = new BigDecimal(20);

    final String URL_LOCALHOST = "http://localhost:8080/checkout";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private SpecialDealRepository specialDealRepository;

    @Autowired
    private ItemRepository itemRepository;

    private RestTemplate restTemplate = new TestRestTemplate().getRestTemplate();

    private Item itemWithSpecialDeal;
    private Item item;
    private SpecialDeal specialDeal;

    @Before
    public void setup() {
        itemWithSpecialDeal = getItem("1");
        assertEquals(SKU_SPECIALS, itemWithSpecialDeal.getItemSku());
        item = getItem("2");
        assertEquals(SKU_NO_SPECIALS, item.getItemSku());
        specialDeal = getSpecialDeal();
    }

    @Test
    public void contextLoads() {
        assertEquals(1, specialDealRepository.count());
        assertEquals(2, itemRepository.count());
    }

    @Test
    public void purchaseCompleted_verifyTotal() {
        int NUMBER_ITEMS_SPECIAL_DEAL = 4;
        int NUMBER_ITEMS_STANDARD = 6;
        scanSomeItems(NUMBER_ITEMS_SPECIAL_DEAL, NUMBER_ITEMS_STANDARD);
        Purchase purchase = getPurchase();
        assertNotNull(purchase);
        assertEquals(BigInteger.valueOf(25 + 10 + 120), purchase.getTotalAmount().toBigInteger());
    }


    @Test
    public void purchaseCompleted_verifyTotalx() {
        int NUMBER_ITEMS_SPECIAL_DEAL = 4;
        int NUMBER_ITEMS_STANDARD = 6;
        int deals = getNumberOfTimesDiscountApplied(NUMBER_ITEMS_SPECIAL_DEAL, specialDeal.getQuantity());
        assertEquals(1, deals);
        BigDecimal moneyForSpecialDeals = specialDeal.getSpecialPrice().multiply(BigDecimal.valueOf(deals));
        assertEquals(SPECIAL_PRICE.toBigInteger(), moneyForSpecialDeals.toBigInteger());
        assertEquals(BigDecimal.valueOf(25).toBigInteger(), moneyForSpecialDeals.toBigInteger());

        int remainingItemsNoSpecial = NUMBER_ITEMS_SPECIAL_DEAL - deals * specialDeal.getQuantity();
        assertEquals(1, remainingItemsNoSpecial);

        BigDecimal remainingMoneyOnItemsWithSpecials = BigDecimal.valueOf(remainingItemsNoSpecial).multiply(itemWithSpecialDeal.getStandardPrice());
        assertEquals(itemWithSpecialDeal.getStandardPrice().toBigInteger(), remainingMoneyOnItemsWithSpecials.toBigInteger());
        assertEquals(BigDecimal.TEN.toBigInteger(), remainingMoneyOnItemsWithSpecials.toBigInteger());

        BigDecimal priceOfItemsWithoutSpecialDeal = BigDecimal.valueOf(NUMBER_ITEMS_STANDARD).multiply(item.getStandardPrice());
        assertEquals(BigDecimal.valueOf(120).toBigInteger(), priceOfItemsWithoutSpecialDeal.toBigInteger());

        BigDecimal expectedTotal = moneyForSpecialDeals.add(remainingMoneyOnItemsWithSpecials).add(priceOfItemsWithoutSpecialDeal);
        scanSomeItems(NUMBER_ITEMS_SPECIAL_DEAL, NUMBER_ITEMS_STANDARD);
        Purchase purchase = getPurchase();
        assertNotNull(purchase);
        assertEquals(3, purchase.getPurchaseLines().size());
        assertEquals(expectedTotal.toBigInteger(), purchase.getTotalAmount().toBigInteger());

    }

    @Test
    public void purchaseCompleted_verifyTotalzzzz() {
        int NUMBER_ITEMS_SPECIAL_DEAL = 4;
        int NUMBER_ITEMS_STANDARD = 6;
        scanSomeItems(NUMBER_ITEMS_SPECIAL_DEAL, NUMBER_ITEMS_STANDARD);
        Purchase purchase = getPurchase();
        assertNotNull(purchase);
        assertEquals(3, purchase.getPurchaseLines().size());
        BigDecimal totalAmountForPurchase = BigDecimal.ZERO;
        for (PurchaseLine purchaseLine : purchase.getPurchaseLines()) {
            totalAmountForPurchase = totalAmountForPurchase.add(purchaseLine.getTotalAmount());
        }
        assertEquals(totalAmountForPurchase.toBigInteger(), purchase.getTotalAmount().toBigInteger());
    }

    private void scanSomeItems(int numberItemsWithSpecialDeal, int numberItemsWithoutSpecialDeal) {
        for (int i = 0; i < numberItemsWithSpecialDeal; i++) {
            scanItem(String.valueOf(itemWithSpecialDeal.getId()));
        }
        for (int i = 0; i < numberItemsWithoutSpecialDeal; i++) {
            scanItem(String.valueOf(item.getId()));
        }
    }

    @Test
    public void specialDeal() {
        String get = URL_LOCALHOST + "/api/specials/1";
        assertTrue(StringUtils.contains(get, URL_LOCALHOST + "/"));
        SpecialDeal specialDeal = restTemplate.getForObject(get, SpecialDeal.class);
        assertNotNull(specialDeal);
        assertEquals(QUANTITY_REQUIRED, specialDeal.getQuantity());
    }

    @Test
    public void specialDeals() {
        String get = URL_LOCALHOST + "/api/specials";
        assertTrue(StringUtils.contains(get, URL_LOCALHOST + "/"));
        List<SpecialDeal> specialDeals = Arrays.asList(restTemplate.getForObject(get, SpecialDeal[].class));
        assertNotNull(specialDeals);
        assertEquals(1, specialDeals.size());
        assertEquals(QUANTITY_REQUIRED, specialDeals.get(0).getQuantity());
    }

    @Test
    public void item() {
        String get = URL_LOCALHOST + "/api/items/1";
        assertTrue(StringUtils.contains(get, URL_LOCALHOST + "/"));
        Item item = restTemplate.getForObject(get, Item.class);
        assertNotNull(item);
        assertEquals(SKU_SPECIALS, item.getItemSku());
    }

    @Test
    public void items() {
        String get = URL_LOCALHOST + "/api/listitems";
        assertTrue(StringUtils.contains(get, URL_LOCALHOST + "/"));
        List<Item> items = Arrays.asList(restTemplate.getForObject(get, Item[].class));
        assertNotNull(items);
    }


    private Item getItem(String id) {
        String get = URL_LOCALHOST + "/api/items/" + id;
        assertTrue(StringUtils.contains(get, URL_LOCALHOST + "/"));
        Item item = restTemplate.getForObject(get, Item.class);
        assertNotNull(item);
        return item;
    }

    private SpecialDeal getSpecialDeal() {
        String get = URL_LOCALHOST + "/api/specials/1";
        assertTrue(StringUtils.contains(get, URL_LOCALHOST + "/"));
        SpecialDeal specialDeal = restTemplate.getForObject(get, SpecialDeal.class);
        assertNotNull(specialDeal);
        assertEquals(QUANTITY_REQUIRED, specialDeal.getQuantity());
        return specialDeal;
    }

    private void scanItem(String id) {
        assertTrue(id.equals("1") || id.equals("2"));
        String get = URL_LOCALHOST + "/api/scan/" + id;
        assertTrue(StringUtils.contains(get, URL_LOCALHOST + "/"));
        Map<Item, Integer> itemMap = restTemplate.getForObject(get, HashMap.class);
        assertNotNull(itemMap);
    }

    private Purchase getPurchase() {
        String get = URL_LOCALHOST + "/api/done";
        Purchase purchase = restTemplate.getForObject(get, Purchase.class);
        assertNotNull(purchase);
        return purchase;
    }

    private Purchase getPurchase(String id) {
        String get = URL_LOCALHOST + "/api/purchase/" + id;
        Purchase purchase = restTemplate.getForObject(get, Purchase.class);
        assertNotNull(purchase);
        return purchase;
    }

    private int getNumberOfTimesDiscountApplied(int quantity, int quantityRequiredForSpecial) {
        Float numberOfTimesDiscountApplied = quantity / Float.valueOf(quantityRequiredForSpecial);
        return numberOfTimesDiscountApplied.intValue();
    }


}
