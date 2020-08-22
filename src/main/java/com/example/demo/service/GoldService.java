package com.example.demo.service;

import com.example.demo.dto.GoldDataDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoldService {

    public String getGold(String url){
        RestTemplate restTemplate = new RestTemplate();
        GoldDataDto[] goldDataDto = restTemplate.getForObject(url, GoldDataDto[].class);
        return goldDataDto[0].getCena().toString();
    }
}
