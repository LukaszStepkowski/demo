package com.example.demo.rest;

import com.example.demo.service.GoldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping(path = "/gold")
public class GoldRestController {

    private final GoldService goldService;

    @Autowired
    public GoldRestController(GoldService goldService) {
        this.goldService = goldService;
    }

    @GetMapping
    public BigDecimal getGoldPrice(String url){
        return goldService.getGold(url);
    }
}
