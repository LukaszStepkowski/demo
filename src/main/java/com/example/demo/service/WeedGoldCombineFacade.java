package com.example.demo.service;

import com.example.demo.model.PriceStatPerDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WeedGoldCombineFacade {

    private final GoldService goldService;
    private final FileService statService;
    private final EmailService emailService;

    @Autowired
    public WeedGoldCombineFacade(GoldService goldService, FileService statService, EmailService emailService) {
        this.goldService = goldService;
        this.statService = statService;
        this.emailService = emailService;
    }

    public Map<String, BigDecimal> weedForGold() throws IOException {
        Map<String, Optional<PriceStatPerDay>> statistics = statService.statistics();
        BigDecimal gold = goldService.getGold("http://api.nbp.pl/api/cenyzlota/");

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
}
