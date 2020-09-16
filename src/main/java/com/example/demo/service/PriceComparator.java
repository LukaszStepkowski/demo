package com.example.demo.service;

import com.example.demo.model.PriceStatPerDay;

import java.util.Comparator;

class PriceComparator implements Comparator<PriceStatPerDay> {

    @Override
    public int compare(PriceStatPerDay o1, PriceStatPerDay o2) {
        return o1.getMidQualityPrice().compareTo(o2.getMidQualityPrice());
    }
}
