package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceStatPerDay {

    private String state;
    private BigDecimal highQualityPrice;
    private BigDecimal midQualityPrice;
    private BigDecimal lowQualityPrice;
    private LocalDate date;
}
