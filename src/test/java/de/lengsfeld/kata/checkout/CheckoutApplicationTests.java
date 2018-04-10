package de.lengsfeld.kata.checkout;

import de.lengsfeld.kata.checkout.model.Item;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CheckoutApplicationTests {

    private static final String SKU_SPECIALS = "1SKU";
    private static final BigDecimal STANDARD_PRICE = BigDecimal.TEN;
    private static final BigDecimal SPECIAL_PRICE = BigDecimal.valueOf(25);
    private static final int QUANTITY_REQUIRED = 3;
    private static final String SKU_NO_SPECIALS = "2SKU";
    private static final BigDecimal NO_SPECIALS_PRICE = BigDecimal.valueOf(20);
    final String URL_LOCALHOST = "http://localhost:8080/checkout";
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private SpecialDealRepository specialDealRepository;

    @Autowired
    private ItemRepository itemRepository;


    private RestTemplate restTemplate = new TestRestTemplate().getRestTemplate();

    @Before
    public void setupMockMvc() {
        //mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void contextLoads() {
        assertEquals(1, specialDealRepository.count());
        assertEquals(2, itemRepository.count());
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
    public void scanItem() {
        String get = URL_LOCALHOST + "/api/scan/1";
        assertTrue(StringUtils.contains(get, URL_LOCALHOST + "/"));
        Map<Item, Integer> item = restTemplate.getForObject(get, HashMap.class);
        assertNotNull(item);
        //assertEquals(SKU_SPECIALS, item.getItemSku());
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


}
