package com.example.demo.service;

import com.example.demo.dto.GoldDataDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class GoldService {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    public BigDecimal getGold(String url){
        return REST_TEMPLATE.getForObject(url, GoldDataDto[].class)[0].getCena();
    }
}
