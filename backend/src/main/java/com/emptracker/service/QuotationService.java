package com.emptracker.service;

import com.emptracker.dto.QuotationItemRequest;
import com.emptracker.dto.QuotationRateResponse;
import com.emptracker.dto.QuotationRequest;
import com.emptracker.dto.QuotationResponse;
import com.emptracker.model.Quotation;
import com.emptracker.model.QuotationItem;
import com.emptracker.repository.QuotationRateRepository;
import com.emptracker.repository.QuotationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class QuotationService {

    private final QuotationRepository quotationRepository;
    private final QuotationRateRepository rateRepository;


    public QuotationService(
            QuotationRepository quotationRepository,
            QuotationRateRepository rateRepository) {

        this.quotationRepository = quotationRepository;
        this.rateRepository = rateRepository;
    }


    @Transactional
    public QuotationResponse create(QuotationRequest request) {

        Quotation quotation = new Quotation();


        // ==========================================
        // BASIC QUOTATION DETAILS
        // ==========================================

        quotation.setClientName(
                request.getClientName().trim()
        );

        quotation.setQuotationDate(
                request.getQuotationDate() != null
                        ? request.getQuotationDate()
                        : LocalDate.now()
        );

        quotation.setQuotationNo(
                nextQuotationNumber()
        );


        // ==========================================
        // TO AND FRO CHARGES
        // ==========================================

        quotation.setToAndFroCharges(
                request.getToAndFroCharges()
        );


        // ==========================================
        // BANK DETAILS
        // ==========================================

        quotation.setBankAccountHolderName(
                request.getBankAccountHolderName()
        );

        quotation.setBankAccountNo(
                request.getBankAccountNo()
        );

        quotation.setBankName(
                request.getBankName()
        );

        quotation.setBankBranch(
                request.getBankBranch()
        );

        quotation.setBankIfscCode(
                request.getBankIfscCode()
        );
        
        quotation.setComanyName(
        		request.getCompanyName()
        		);


        // ==========================================
        // QUOTATION ITEMS
        // ==========================================

        if (request.getItems() != null) {

            for (QuotationItemRequest itemRequest : request.getItems()) {

                QuotationItem item = new QuotationItem();

                item.setDescription(
                        itemRequest.description().trim()
                );

                item.setRate(
                        itemRequest.rate()
                );

                item.setUnit(
                        itemRequest.unit()
                );

                item.setQuotation(
                        quotation
                );

                quotation.getItems().add(item);
            }
        }


        // ==========================================
        // SAVE
        // ==========================================

        Quotation saved = quotationRepository.save(quotation);


        // ==========================================
        // RESPONSE
        // ==========================================

        return new QuotationResponse(
                saved.getId(),
                saved.getQuotationNo(),
                saved.getClientName(),
                saved.getQuotationDate()
        );
    }


    @Transactional(readOnly = true)
    public Quotation get(Long id) {

        return quotationRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Quotation not found: " + id
                        )
                );
    }


    @Transactional(readOnly = true)
    public List<QuotationRateResponse> getActiveRates() {

        return rateRepository
                .findByActiveTrueOrderByIdAsc()
                .stream()
                .map(r ->
                        new QuotationRateResponse(
                                r.getId(),
                                r.getDescription(),
                                r.getRate(),
                                r.getUnit()
                        )
                )
                .toList();
    }


    private String nextQuotationNumber() {

        long next = quotationRepository.count() + 1;

        String number;

        do {

            number = String.format(
                    "QTN-%d-%04d",
                    LocalDate.now().getYear(),
                    next++
            );

        } while (
                quotationRepository.existsByQuotationNo(number)
        );

        return number;
    }
}