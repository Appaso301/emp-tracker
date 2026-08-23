package com.emptracker.dto;

import java.math.BigDecimal;

public record QuotationRateResponse(
        Long id,
        String description,
        BigDecimal rate,
        String unit
) {}
