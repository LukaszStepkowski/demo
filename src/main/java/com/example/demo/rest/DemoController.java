package com.example.demo.rest;

import com.example.demo.model.PriceStatPerDay;
import com.example.demo.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

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



}
