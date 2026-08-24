package com.emptracker.controller;

import com.emptracker.dto.QuotationRequest;
import com.emptracker.dto.QuotationResponse;
import com.emptracker.dto.QuotationRateResponse;
import com.emptracker.service.QuotationPdfService;
import com.emptracker.service.QuotationService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quotations")
@CrossOrigin(origins = {
        "http://localhost:4200",
        "http://127.0.0.1:4200",
        "http://localhost:4201",
        "http://127.0.0.1:4201",
        "https://emp-tracker-eosin.vercel.app"
})
public class QuotationController {
    private final QuotationService quotationService;
    private final QuotationPdfService quotationPdfService;

    public QuotationController(QuotationService quotationService,
                                QuotationPdfService quotationPdfService) {
        this.quotationService = quotationService;
        this.quotationPdfService = quotationPdfService;
    }

    @GetMapping("/rates")
    public List<QuotationRateResponse> getRates() {
        return quotationService.getActiveRates();
    }

    @PostMapping
    public ResponseEntity<QuotationResponse> create(@Valid @RequestBody QuotationRequest request) {
        return ResponseEntity.ok(quotationService.create(request));
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> pdf(@PathVariable Long id) throws Exception {

        byte[] pdf = quotationPdfService.generate(id);

        String fileName = quotationService
                .get(id)
                .getQuotationNo() + ".pdf";

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\""
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
