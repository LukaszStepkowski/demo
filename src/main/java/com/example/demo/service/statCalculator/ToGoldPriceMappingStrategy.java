package com.example.demo.service.statCalculator;

import com.example.demo.service.GoldService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.MathContext;

@RequiredArgsConstructor
class ToGoldPriceMappingStrategy implements StatMappingStrategy {
    public static final String CENA_ZLOTA = "http://api.nbp.pl/api/cenyzlota/";

    private final GoldService goldService;

    @Override
    public BigDecimal map(BigDecimal statToMap) {
        return statToMap.divide(goldService.getGold(CENA_ZLOTA), MathContext.DECIMAL32);
    }
}
