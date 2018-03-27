package com.scleroid.financematic.utils;

public class CurrencyStringUtils {
    public CurrencyStringUtils() {
    }

    public String bindNumber(String amount) {
        return String.format("₹ %s", amount);
    }

    public String bindNumber(int amount) {
        return String.format("₹ %d", amount);
    }
}