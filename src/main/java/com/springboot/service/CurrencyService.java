package com.springboot.service;

import java.util.*;

import com.springboot.model.CurrencyList;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class CurrencyService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.api.url}")
    private String apiUrl;

    @Autowired
    @Qualifier("ehCacheManager")
    private net.sf.ehcache.CacheManager ehCacheManager;

    /**
     * fetch Currency from URL or cache based on config
     *
     * @return Map<String, Double>
     */
    public Map<String, Double> fetchCurrency(String currencyType, Integer cacheInterval) throws InterruptedException {

        Ehcache ehcache = ehCacheManager.getCache("currency-cache");
        Element element = ehcache.get("Cache"+currencyType);
        Map<String ,Double> data = null;

        if(Objects.nonNull(element)){
            data = (Map<String ,Double>) element.getObjectValue();
            log.info("Get Cached Data of {} ", element.getObjectValue());
        } else {
            CurrencyList currencyList = getCurrencyList();
            data = Collections.singletonMap(currencyType, currencyList.getRates().get(currencyType));
            log.info("Get Data from url {} , {} ", apiUrl, data);
        }

        if(cacheInterval != null){
            element = new Element("Cache"+currencyType , data , 3600,  cacheInterval);
            ehcache.put(element);
        }

        return data;
    }


    /**
     * get Currency List from external
     *
     * @return CurrencyList
     */

    public CurrencyList getCurrencyList() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        ResponseEntity<CurrencyList> responseEntity = restTemplate.exchange(apiUrl,
                HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<CurrencyList>() {
                });
        return responseEntity.getBody();
    }

}
