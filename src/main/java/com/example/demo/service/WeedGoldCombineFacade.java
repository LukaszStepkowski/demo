package com.example.demo.service;

import com.example.demo.model.PriceStatPerDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WeedGoldCombineFacade {

    public static final String CENA_ZLOTA = "http://api.nbp.pl/api/cenyzlota/";
    private final GoldService goldService;
    private final FileService statService;
    private final EmailService emailService;
    private final CurrencyConverter currencyConverter;
    private final CurrencyRetriever currencyRetriever = new CurrencyRetriever();

    @Autowired
    public WeedGoldCombineFacade(GoldService goldService, FileService statService, EmailService emailService, CurrencyConverter currencyConverter) {
        this.goldService = goldService;
        this.statService = statService;
        this.emailService = emailService;
        this.currencyConverter = currencyConverter;
    }

    public Map<String, BigDecimal> weedForGold() throws IOException {
        Map<String, Optional<PriceStatPerDay>> statistics = statService.statistics();
        BigDecimal gold = goldService.getGold(CENA_ZLOTA);

        if (gold.compareTo(BigDecimal.ONE) < 0) {
            emailService.sendEmail();
        } else {
            return statistics.entrySet().stream()
                    .filter(e -> e.getValue().isPresent())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> e.getValue().get().getLowQualityPrice().divide(gold, MathContext.DECIMAL32)
                    ));
        }
        return Map.of();
    }

    public Map<String, BigDecimal> goldBased() throws IOException {
        return rawData().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, v -> v.getValue()
                        .divide(goldService.getGold(CENA_ZLOTA),
                                MathContext.DECIMAL32)));
    }

    public Map<String, BigDecimal> rawData() throws IOException {
        return statService.readPrices().stream()
                .filter(p -> p.getLowQualityPrice() != null)
                .collect(Collectors.groupingBy(p -> p.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        Collectors.mapping(PriceStatPerDay::getLowQualityPrice,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));
    }

    public Map<String, BigDecimal> currencyBased(String currency) throws IOException {
        return rawData().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        v -> currencyConverter.convertCurrency(currencyRetriever.retrieveCurrentExchangeRate("USD"),
                                currencyRetriever.retrieveCurrentExchangeRate(currency),
                                v.getValue())));
    }
}
