package com.springboot.smoketest;


import com.springboot.service.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalTime;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class ST_CurrencyService {

    @Autowired
    private CurrencyService currencyService;

    @Value("${app.api.url}")
    private String apiUrl;

    @Test
    void check_currencyService() {
        try {
            log.info("Start the Smoke Test");
            log.info("Set currencyType EUR and Set Cache Interval 10 seconds");

            Map<String, Double> currencyValue = currencyService.fetchCurrency("EUR", 10);
            Assert.assertNotNull(currencyValue);
            Assert.assertNotNull(currencyValue.get("EUR"));


            log.info("Before Expire Cache , Time {} ", LocalTime.now());

            currencyService.fetchCurrency("EUR", null);
            Assert.assertNotNull(currencyValue);
            Assert.assertNotNull(currencyValue.get("EUR"));

            Thread.sleep(10000);

            log.info("After Expire Cache, Time {} ", LocalTime.now());
            currencyService.fetchCurrency("EUR", null);
            Assert.assertNotNull(currencyValue);
            Assert.assertNotNull(currencyValue.get("EUR"));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }





}
