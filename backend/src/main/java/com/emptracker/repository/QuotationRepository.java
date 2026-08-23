package com.emptracker.repository;

import com.emptracker.model.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuotationRepository extends JpaRepository<Quotation, Long> {
    boolean existsByQuotationNo(String quotationNo);
}
