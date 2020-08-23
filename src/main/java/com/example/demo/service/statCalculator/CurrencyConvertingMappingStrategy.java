package com.example.demo.service.statCalculator;

import com.example.demo.service.CurrencyConverter;
import com.example.demo.service.CurrencyRetriever;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
class CurrencyConvertingMappingStrategy implements StatMappingStrategy {
    private static final CurrencyRetriever CURRENCY_RETRIEVER = new CurrencyRetriever();

    private final CurrencyConverter currencyConverter;
    private final String currency;

    @Override
    public BigDecimal map(BigDecimal statToMap) {
        return currencyConverter.convertCurrency(
                    CURRENCY_RETRIEVER.retrieveCurrentExchangeRate("USD"),
                    CURRENCY_RETRIEVER.retrieveCurrentExchangeRate(currency),
                    statToMap
                );
    }
}
