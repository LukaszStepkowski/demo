package com.example.demo.service.statCalculator;

import java.math.BigDecimal;

@FunctionalInterface
public interface StatMappingStrategy {
    BigDecimal map(BigDecimal statToMap);
}
