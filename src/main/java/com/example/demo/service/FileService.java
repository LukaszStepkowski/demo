package com.example.demo.service;

import com.example.demo.model.PriceStatPerDay;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FileService {

    private final Resource resourceFile;

    public FileService(@Value("classpath:marijuana-street-price-clean.csv") Resource resourceFile) {
        this.resourceFile = resourceFile;
    }

    public String fileContent() throws IOException {
        return Files.readString(resourceFile.getFile().toPath());
    }

    public List<PriceStatPerDay> readPrices() throws IOException {
        List<String> strings = Files.readAllLines(resourceFile.getFile().toPath());
        return strings.stream()
                .filter(Objects::nonNull)
                .map(line -> line.split(","))
                .map(array -> new PriceStatPerDay(
                        array[0],
                        getPriceBigDecimal(array[1]),
                        getPriceBigDecimal(array[3]),
                        getPriceBigDecimal(array[5]),
                        getDate(array[7])
                ))
                .collect(Collectors.toList());
    }

    private LocalDate getDate(String s) {
        if (s == null) return null;
        return LocalDate.parse(s);
    }

    private BigDecimal getPriceBigDecimal(String s) {
        if (s == null) return null;
        try {
            return new BigDecimal(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Map<LocalDate, Optional<PriceStatPerDay>> statistics() throws IOException {
        return readPrices().stream()
                .filter(p -> p.getLowQualityPrice() != null)
//                .sorted(Comparator.comparing(PriceStatPerDay::getMidQualityPrice))
                .collect(Collectors.groupingBy(PriceStatPerDay::getDate,
                        Collectors.minBy(Comparator.comparing(PriceStatPerDay::getLowQualityPrice))));
    }
}
