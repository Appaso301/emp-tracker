package com.emptracker.config;

import com.emptracker.model.Advance;
import com.emptracker.model.AttendanceRecord;
import com.emptracker.model.Employee;
import com.emptracker.model.Expense;
import com.emptracker.model.Location;
import com.emptracker.model.TravelCharge;
import com.emptracker.repository.AdvanceRepository;
import com.emptracker.repository.AttendanceRecordRepository;
import com.emptracker.repository.EmployeeRepository;
import com.emptracker.repository.ExpenseRepository;
import com.emptracker.repository.LocationRepository;
import com.emptracker.repository.TravelChargeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer {

    private final LocationRepository locationRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceRecordRepository attendanceRepository;
    private final AdvanceRepository advanceRepository;
    private final ExpenseRepository expenseRepository;
    private final TravelChargeRepository travelChargeRepository;

    public DataInitializer(LocationRepository locationRepository,
                           EmployeeRepository employeeRepository,
                           AttendanceRecordRepository attendanceRepository,
                           AdvanceRepository advanceRepository,
                           ExpenseRepository expenseRepository,
                           TravelChargeRepository travelChargeRepository) {
        this.locationRepository = locationRepository;
        this.employeeRepository = employeeRepository;
        this.attendanceRepository = attendanceRepository;
        this.advanceRepository = advanceRepository;
        this.expenseRepository = expenseRepository;
        this.travelChargeRepository = travelChargeRepository;
    }

    @PostConstruct
    public void init() {
        boolean hasSeedData = locationRepository.count() > 0
                || employeeRepository.count() > 0
                || attendanceRepository.count() > 0
                || advanceRepository.count() > 0
                || expenseRepository.count() > 0
                || travelChargeRepository.count() > 0;

        if (hasSeedData) {
            return;
        }

        Location mumbai = locationRepository.save(new Location("Mumbai Unit", "Andheri East"));
        Location pune = locationRepository.save(new Location("Pune Unit", "Hinjewadi"));
        Location nagpur = locationRepository.save(new Location("Nagpur Unit", "Viman Nagar"));

        Employee alice = employeeRepository.save(new Employee("Alice Sharma", "9876543210", mumbai, new BigDecimal("500"), new BigDecimal("130"), new BigDecimal("195")));
        Employee raj = employeeRepository.save(new Employee("Raj Patil", "9820012345", pune, new BigDecimal("500"), new BigDecimal("120"), new BigDecimal("180")));
        Employee priya = employeeRepository.save(new Employee("Priya Deshmukh", "9898989898", nagpur, new BigDecimal("500"), new BigDecimal("125"), new BigDecimal("187.5")));

        attendanceRepository.save(new AttendanceRecord(alice, LocalDateTime.of(2026, 7, 21, 8, 45), LocalDateTime.of(2026, 7, 21, 18, 15), LocalDate.of(2026, 7, 21), 9.5, 1.5));
        attendanceRepository.save(new AttendanceRecord(raj, LocalDateTime.of(2026, 7, 21, 9, 0), LocalDateTime.of(2026, 7, 21, 17, 30), LocalDate.of(2026, 7, 21), 8.5, 0.5));
        attendanceRepository.save(new AttendanceRecord(priya, LocalDateTime.of(2026, 7, 21, 8, 30), LocalDateTime.of(2026, 7, 21, 18, 30), LocalDate.of(2026, 7, 21), 10.0, 2.0));

        advanceRepository.save(new Advance(alice, new BigDecimal("5000"), LocalDate.of(2026, 7, 20), "July advance"));
        expenseRepository.save(new Expense(raj, new BigDecimal("200"), LocalDate.of(2026, 7, 20), "Lunch", "Field expense"));
        travelChargeRepository.save(new TravelCharge(priya, new BigDecimal("300"), LocalDate.of(2026, 7, 21), "Office", "Site", "Local travel"));
    }
}
