package com.example.demo.rest;

import com.example.demo.model.PriceStatPerDay;
import com.example.demo.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class DemoController {

    private final FileService fileService;

    @Autowired
    public DemoController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping
    public String fileContent () throws IOException {
        return fileService.fileContent();
    }

    @GetMapping(path = "/prices")
    public List<PriceStatPerDay> readPrices () throws IOException {
        return fileService.readPrices();
    }

    @GetMapping(path = "/stats")
    public Map<String, Optional<PriceStatPerDay>> statistics() throws IOException {
        return fileService.statistics();
    }

    @GetMapping(path = "/ammount")
    public BigDecimal calculateMoney() throws IOException {
        return fileService.calculateMoney();
    }


}
