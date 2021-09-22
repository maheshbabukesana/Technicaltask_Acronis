package com.springboot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.model.CurrencyList;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class UT_CurrencyService {

    @Mock
    RestTemplate restTemplate;

    @Mock
    net.sf.ehcache.CacheManager ehCacheManager;

    @InjectMocks
    CurrencyService currencyService;

     private static final String apiUrl = "/v1/latest?access_key=badb1d288f85e967d39a16b0305ed7a1";


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(currencyService, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(currencyService, "ehCacheManager", ehCacheManager);
        ReflectionTestUtils.setField(currencyService, "apiUrl", apiUrl);
    }

    @Test
    public void check_getCurrencyList() {
        Mockito.when(restTemplate.exchange(Mockito.contains(apiUrl),
                Mockito.any(HttpMethod.class), Mockito.any(HttpEntity.class),Mockito.any(ParameterizedTypeReference.class))).thenReturn(new ResponseEntity<>(getCurrencyList(), HttpStatus.OK));
        CurrencyList currencyList =currencyService.getCurrencyList();
        Assert.assertNotNull(currencyList);
        Assert.assertNotNull(currencyList.getRates().get("EUR"));
    }


    private CurrencyList getCurrencyList() {
        ObjectMapper mapper = new ObjectMapper();
        CurrencyList currencyList = new CurrencyList();
        try {
            String payrollDetailsJson = FileUtils.readFileToString(new File("src/test/resources/currencyList.json"), StandardCharsets.UTF_8);
            currencyList = mapper.readValue(payrollDetailsJson, CurrencyList.class);
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
        return currencyList;
    }

}
