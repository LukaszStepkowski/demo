package com.example.demo.service;

import com.example.demo.model.PriceStatPerDay;
import com.example.demo.service.statCalculator.StatMappingStrategy;
import com.example.demo.service.statCalculator.StatMappingStrategyFactory;
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

    private final FileService statService;
    private final EmailService emailService;
    private final StatMappingStrategyFactory strategyFactory;

    @Autowired
    public WeedGoldCombineFacade(FileService statService, EmailService emailService, StatMappingStrategyFactory strategyFactory) {
        this.statService = statService;
        this.emailService = emailService;
        this.strategyFactory = strategyFactory;
    }

    public Map<String, BigDecimal> weedForGold() throws IOException {
        Map<String, Optional<PriceStatPerDay>> statistics = statService.statistics();
        BigDecimal gold = BigDecimal.ONE;//goldService.getGold(CENA_ZLOTA);

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



    public Map<String, BigDecimal> rawData() throws IOException {
        return statService.readPrices().stream()
                .filter(p -> p.getLowQualityPrice() != null)
                .collect(Collectors.groupingBy(p -> p.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        Collectors.mapping(PriceStatPerDay::getLowQualityPrice,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));
    }

    public Map<String, BigDecimal> goldBased() throws IOException {
        return mapUsingStrategy(strategyFactory.goldBasedStrategy());
    }

    public Map<String, BigDecimal> currencyBased(String currency) throws IOException {
        return mapUsingStrategy(strategyFactory.currencyConvertingStrategy(currency));
    }

    public Map<String, BigDecimal> mapUsingStrategy(StatMappingStrategy strategy) throws IOException {
        return rawData().entrySet().stream()
                .collect(
                        Collectors.toMap(Map.Entry::getKey,
                        v -> strategy.map(v.getValue()))
                );
    }
}
