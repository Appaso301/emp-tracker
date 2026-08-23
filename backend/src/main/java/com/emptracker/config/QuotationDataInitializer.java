package com.emptracker.config;

import com.emptracker.model.QuotationRate;
import com.emptracker.repository.QuotationRateRepository;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.math.BigDecimal;

@Component
public class QuotationDataInitializer {
    private final QuotationRateRepository repository;

    public QuotationDataInitializer(QuotationRateRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void init() {
        if (repository.count() > 0) return;

        add("Blow Room", "200000", "per Line");
        add("Carding", "40000", "per card");
        add("Br. Draw Frame", "15000", "per machine");
        add("Fin. Draw Frame", "10000", "per machine");
        add("Speed Frame (with OHTC)", "150000", "per machine");
        add("Ring Frame (with OHTC)", "85", "per spindle");
        add("Link Coner (with OHTC)", "35000", "per machine");
        add("All LUWA Plant", "40", "per spindle");
        add("BTS", "100000", "per quotation");
    }

    private void add(String description, String rate, String unit) {
        QuotationRate item = new QuotationRate();
        item.setDescription(description);
        item.setRate(new BigDecimal(rate));
        item.setUnit(unit);
        item.setActive(true);
        repository.save(item);
    }
}
