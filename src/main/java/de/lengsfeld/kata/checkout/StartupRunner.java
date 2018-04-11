package de.lengsfeld.kata.checkout;

import de.lengsfeld.kata.checkout.model.Item;
import de.lengsfeld.kata.checkout.model.SpecialDeal;
import de.lengsfeld.kata.checkout.repository.ItemRepository;
import de.lengsfeld.kata.checkout.repository.PurchaseRepository;
import de.lengsfeld.kata.checkout.repository.SpecialDealRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.math.BigDecimal;
import java.util.List;

public class StartupRunner implements CommandLineRunner {

    private static final String SKU_SPECIALS = "1SKU";
    private static final BigDecimal STANDARD_PRICE = BigDecimal.TEN;
    private static final BigDecimal SPECIAL_PRICE = BigDecimal.valueOf(25);
    private static final Integer QUANTITY_REQUIRED = 3;
    private static final String SKU_NO_SPECIALS = "2SKU";
    private static final BigDecimal NO_SPECIALS_PRICE = BigDecimal.valueOf(20);
    protected final Log log = LogFactory.getLog(getClass());

    @Autowired
    private SpecialDealRepository specialDealsRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;


    @Override
    public void run(String... args) {
        log.info("Number of special deals: " + specialDealsRepository.count());

        Item itemWithSpecialDeal = new Item(SKU_SPECIALS, STANDARD_PRICE);
        itemRepository.save(itemWithSpecialDeal);
        Item item = new Item(SKU_NO_SPECIALS, NO_SPECIALS_PRICE);
        itemRepository.save(item);


        SpecialDeal specialDeal = new SpecialDeal(itemWithSpecialDeal, QUANTITY_REQUIRED, SPECIAL_PRICE);
        specialDealsRepository.save(specialDeal);

        testthis();
    }

    public void testthis() {
        List<SpecialDeal> specialDeals = specialDealsRepository.findAll();
        for (SpecialDeal specialDeal : specialDeals) {
            log.info(specialDeal.toString());
        }

        //List<Item> itemList = itemsRepository.findAll();
        //for (Item item : itemList) {
        //    SpecialDeal specialDeal = specialDealsRepository.findSpecialDealByItem(item);
        //   log.info("Special deal for: " + item.getItemSku() + "is available!");
        //}

    }

}