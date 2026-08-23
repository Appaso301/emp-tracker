package com.emptracker.repository;

import com.emptracker.model.TravelCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TravelChargeRepository extends JpaRepository<TravelCharge, Long> {
    List<TravelCharge> findByEmployeeIdAndDateBetween(Long employeeId, LocalDate start, LocalDate end);
}
