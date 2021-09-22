package com.springboot.model;

import lombok.Data;

import java.util.Map;

@Data
public class CurrencyList {

    private boolean success;
    private long timestamp;
    private String base;
    private String date;
    private Map<String ,Double> rates;

}
