package de.lengsfeld.kata.checkout;

import de.lengsfeld.kata.checkout.repository.SpecialDealsRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CheckoutApplication {

    protected final Log log = LogFactory.getLog(getClass());

    @Autowired
    private SpecialDealsRepository specialDealsRepository;

    public static void main(String[] args) {
        SpringApplication.run(CheckoutApplication.class, args);
    }

}
