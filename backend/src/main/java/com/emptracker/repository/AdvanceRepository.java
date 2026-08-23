package com.emptracker.repository;

import com.emptracker.model.Advance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AdvanceRepository extends JpaRepository<Advance, Long> {
    List<Advance> findByEmployeeIdAndDateBetween(Long employeeId, LocalDate start, LocalDate end);
}
