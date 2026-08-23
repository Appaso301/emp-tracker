package com.emptracker.dto;

import java.time.LocalDate;

public record QuotationResponse(
        Long id,
        String quotationNo,
        String clientName,
        LocalDate quotationDate
) {}
