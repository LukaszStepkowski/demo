package com.example.demo.rest;

import com.example.demo.service.GoldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/gold")
public class GoldRestController {

    @Autowired
    GoldService goldService;

    @GetMapping
    public String GetGoldPrice (String url){
        return goldService.getGold(url);
    }
}
