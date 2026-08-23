package com.emptracker.repository;

import com.emptracker.model.QuotationRate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuotationRateRepository extends JpaRepository<QuotationRate, Long> {
    List<QuotationRate> findByActiveTrueOrderByIdAsc();
}
