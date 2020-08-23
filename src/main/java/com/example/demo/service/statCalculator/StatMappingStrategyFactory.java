package com.example.demo.service.statCalculator;

import com.example.demo.service.CurrencyConverter;
import com.example.demo.service.GoldService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatMappingStrategyFactory {

    private final GoldService goldService;
    private final CurrencyConverter currencyConverter;

    public StatMappingStrategy currencyConvertingStrategy(String currency) {
        return new CurrencyConvertingMappingStrategy(currencyConverter, currency);
    }

    public StatMappingStrategy goldBasedStrategy() {
        return new ToGoldPriceMappingStrategy(goldService);
    }

}
