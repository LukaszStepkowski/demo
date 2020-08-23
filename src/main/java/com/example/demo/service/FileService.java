package com.example.demo.service;

import com.example.demo.model.PriceStatPerDay;
import com.sun.el.lang.FunctionMapperImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                        parseFromString2(BigDecimal.ONE, () -> new BigDecimal(array[1])),
                        parseFromString(BigDecimal.ONE, array[3], BigDecimal::new),
                        parseFromString(BigDecimal.ONE, array[5], BigDecimal::new),
                        getDate(array[7]),
                        parseFromString(BigInteger.ZERO, array[2], BigInteger::new),
                        parseFromString(BigInteger.ZERO, array[4], BigInteger::new),
                        parseFromString(BigInteger.ZERO, array[6], BigInteger::new)
                ))
                .collect(Collectors.toList());
    }

//    private BigInteger getCountBigInt(String s) {
//        if (s == null) return BigInteger.ZERO;
//        try {
//            return new BigInteger(s);
//        } catch (NumberFormatException e) {
//            return BigInteger.ZERO;
//        }
//    }
//
//    private BigDecimal getPriceBigDecimal(String s) {
//        if (s == null) return BigDecimal.ONE;
//        try {
//            return new BigDecimal(s);
//        } catch (NumberFormatException e) {
//            return BigDecimal.ONE;
//        }
//    }

    private <T extends Number> T parseFromString(T defaultValue, String s, Function<String, T> creator) {
        if (s == null) return defaultValue;
        try {
            return creator.apply(s);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private <T extends Number> T parseFromString2(T defaultValue, Supplier<T> supplier) {
//        if (s == null) return defaultValue;
        try {
            return supplier.get();
        } catch (NumberFormatException | NullPointerException e) {
            return defaultValue;
        }
    }

    private LocalDate getDate(String s) {
        if (s == null) return null;
        return LocalDate.parse(s);
    }

    public Map<String, Optional<PriceStatPerDay>> statistics() throws IOException {
        return readPrices().stream()
                .filter(p -> p.getLowQualityPrice() != null)
//                .sorted(Comparator.comparing(PriceStatPerDay::getMidQualityPrice))
                .collect(Collectors.groupingBy(p -> p.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        Collectors.minBy(Comparator.comparing(PriceStatPerDay::getLowQualityPrice))));
    }

    public BigDecimal calculateMoney() throws IOException {
        return readPrices().stream()
                .flatMap(p -> Stream.of(
                        p.getHighQualityPrice().multiply(new BigDecimal(p.getHighCount())),
                        p.getMidQualityPrice().multiply(new BigDecimal(p.getMidCount())),
                        p.getLowQualityPrice().multiply(new BigDecimal(p.getLowCount()))
                ))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
