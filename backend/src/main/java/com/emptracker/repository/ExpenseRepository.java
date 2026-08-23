package com.emptracker.repository;

import com.emptracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByEmployeeIdAndDateBetween(Long employeeId, LocalDate start, LocalDate end);
}
