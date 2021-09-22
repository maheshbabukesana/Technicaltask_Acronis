package com.springboot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.springboot.service.CurrencyService;
import java.util.Map;

@RestController
@RequestMapping("/rest")
@Slf4j
public class CurrencyController {

	@Autowired
	private CurrencyService aPIService;

	@Autowired
	@Qualifier("ehCacheManager")
	private net.sf.ehcache.CacheManager ehCacheManager;

	@GetMapping(value = "/getCurrency", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String ,Double>> fetchCurrency(@RequestParam(name = "currencyType") String currencyType, @RequestParam(name = "setCacheInterval", required = false)  Integer cacheInterval) throws InterruptedException {
		return new ResponseEntity<>( aPIService.fetchCurrency(currencyType, cacheInterval), HttpStatus.OK);

	}

}