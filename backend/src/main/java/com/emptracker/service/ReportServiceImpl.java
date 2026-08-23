package com.emptracker.service;

import com.emptracker.dto.ReportSummary;
import com.emptracker.model.Advance;
import com.emptracker.model.AttendanceRecord;
import com.emptracker.model.Employee;
import com.emptracker.model.Expense;
import com.emptracker.model.TravelCharge;
import com.emptracker.repository.AdvanceRepository;
import com.emptracker.repository.AttendanceRecordRepository;
import com.emptracker.repository.EmployeeRepository;
import com.emptracker.repository.ExpenseRepository;
import com.emptracker.repository.TravelChargeRepository;
import org.springframework.stereotype.Service;
import com.emptracker.dto.LocationReportResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ReportServiceImpl implements ReportService {

    private final EmployeeRepository employeeRepository;
    private final AttendanceRecordRepository attendanceRepository;
    private final AdvanceRepository advanceRepository;
    private final ExpenseRepository expenseRepository;
    private final TravelChargeRepository travelChargeRepository;

    public ReportServiceImpl(
            EmployeeRepository employeeRepository,
            AttendanceRecordRepository attendanceRepository,
            AdvanceRepository advanceRepository,
            ExpenseRepository expenseRepository,
            TravelChargeRepository travelChargeRepository) {

        this.employeeRepository = employeeRepository;
        this.attendanceRepository = attendanceRepository;
        this.advanceRepository = advanceRepository;
        this.expenseRepository = expenseRepository;
        this.travelChargeRepository = travelChargeRepository;
    }

    @Override
    public List<ReportSummary> employeeReport(
            Long employeeId,
            LocalDate start,
            LocalDate end) {

        Optional<Employee> employeeOpt = employeeRepository.findById(
                Objects.requireNonNull(
                        employeeId,
                        "Employee id must not be null"
                )
        );

        if (employeeOpt.isEmpty()) {
            return Collections.emptyList();
        }

        Employee employee = employeeOpt.get();

        ReportSummary summary =
                buildSummary(employee, start, end);

        return List.of(summary);
    }

    @Override
    public LocationReportResponse locationReport(
            Long locationId,
            LocalDate start,
            LocalDate end) {

        List<Employee> employees =
                employeeRepository.findByLocationId(locationId);

        List<ReportSummary> reports = employees.stream()
                .map(employee -> buildSummary(employee, start, end))
                .toList();

        BigDecimal totalNetPay = reports.stream()
                .map(ReportSummary::getNetPay)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new LocationReportResponse(
                reports,
                totalNetPay
        );
    }

    @Override
    public ReportSummary consolidatedReport(
            LocalDate start,
            LocalDate end) {

        List<Employee> employees =
                employeeRepository.findAll();

        ReportSummary consolidated =
                new ReportSummary();

        consolidated.setEmployeeName("All employees");
        consolidated.setLocationName("All locations");
        consolidated.setEmployeeId(0L);

        BigDecimal totalGross = BigDecimal.ZERO;
        BigDecimal totalAdvances = BigDecimal.ZERO;
        BigDecimal totalExpenses = BigDecimal.ZERO;
        BigDecimal totalTravel = BigDecimal.ZERO;

        double totalDays = 0;
        double totalOvertime = 0;

        for (Employee employee : employees) {

            ReportSummary summary =
                    buildSummary(employee, start, end);

            totalDays += summary.getTotalHours() != null
                    ? summary.getTotalHours()
                    : 0;

            totalOvertime += summary.getTotalOvertime() != null
                    ? summary.getTotalOvertime()
                    : 0;

            totalGross = totalGross.add(
                    summary.getGrossPay() != null
                            ? summary.getGrossPay()
                            : BigDecimal.ZERO
            );

            totalAdvances = totalAdvances.add(
                    summary.getAdvances() != null
                            ? summary.getAdvances()
                            : BigDecimal.ZERO
            );

            totalExpenses = totalExpenses.add(
                    summary.getExpenses() != null
                            ? summary.getExpenses()
                            : BigDecimal.ZERO
            );

            totalTravel = totalTravel.add(
                    summary.getTravelCharges() != null
                            ? summary.getTravelCharges()
                            : BigDecimal.ZERO
            );
        }

        consolidated.setTotalHours(totalDays);
        consolidated.setTotalOvertime(totalOvertime);

        consolidated.setGrossPay(totalGross);
        consolidated.setAdvances(totalAdvances);
        consolidated.setExpenses(totalExpenses);
        consolidated.setTravelCharges(totalTravel);

        BigDecimal netPay =
                totalGross
                        .subtract(totalAdvances)
                        .subtract(totalExpenses)
                        .add(totalTravel);

        consolidated.setNetPay(netPay);

        return consolidated;
    }

    private ReportSummary buildSummary(
            Employee employee,
            LocalDate start,
            LocalDate end) {

        // =====================================================
        // GET ATTENDANCE
        // =====================================================

        List<AttendanceRecord> attendance =
                attendanceRepository
                        .findByEmployeeIdAndWorkDateBetween(
                                employee.getId(),
                                start,
                                end
                        );

        // =====================================================
        // GET ADVANCES
        // =====================================================

        List<Advance> advances =
                advanceRepository
                        .findByEmployeeIdAndDateBetween(
                                employee.getId(),
                                start,
                                end
                        );

        // =====================================================
        // GET EXPENSES
        // =====================================================

        List<Expense> expenses =
                expenseRepository
                        .findByEmployeeIdAndDateBetween(
                                employee.getId(),
                                start,
                                end
                        );

        // =====================================================
        // GET TRAVEL CHARGES
        // =====================================================

        List<TravelCharge> travelCharges =
                travelChargeRepository
                        .findByEmployeeIdAndDateBetween(
                                employee.getId(),
                                start,
                                end
                        );

        // =====================================================
        // 1. NUMBER OF DAYS
        //
        // In your current data model, hoursWorked contains
        // the number of working days.
        //
        // Example:
        // hoursWorked = 30
        // means 30 working days.
        // =====================================================

        double numberOfDays = attendance.stream()
                .filter(Objects::nonNull)
                .mapToDouble(record ->
                        record.getHoursWorked() != null
                                ? record.getHoursWorked()
                                : 0.0
                )
                .sum();

        // =====================================================
        // 2. TOTAL OVERTIME HOURS
        // =====================================================

        double totalOvertimeHours = attendance.stream()
                .filter(Objects::nonNull)
                .mapToDouble(record ->
                        record.getOvertimeHours() != null
                                ? record.getOvertimeHours()
                                : 0.0
                )
                .sum();

        // =====================================================
        // 3. CONVERT OVERTIME HOURS TO DAYS
        //
        // 8 overtime hours = 1 overtime day
        // =====================================================

        double overtimeDays =
                totalOvertimeHours / 8.0;

        // =====================================================
        // 4. TOTAL PAYABLE DAYS
        //
        // Number of Days + Overtime Days
        // =====================================================

        double totalPayableDays =
                numberOfDays + overtimeDays;

        // =====================================================
        // 5. GET DAILY RATE
        //
        // Only dailyRate is used.
        //
        // hourlyRate and overtimeRate are NOT used.
        // =====================================================

        BigDecimal dailyRate =
                employee.getDailyRate() != null
                        ? employee.getDailyRate()
                        : BigDecimal.ZERO;

        // =====================================================
        // 6. CALCULATE GROSS PAY
        //
        // Gross Pay =
        // Total Payable Days × Daily Rate
        // =====================================================

        BigDecimal grossPay =
                dailyRate.multiply(
                        BigDecimal.valueOf(totalPayableDays)
                );

        // =====================================================
        // 7. CALCULATE ADVANCES
        // =====================================================

        BigDecimal totalAdvances =
                advances.stream()
                        .filter(Objects::nonNull)
                        .map(Advance::getAmount)
                        .filter(Objects::nonNull)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        // =====================================================
        // 8. CALCULATE EXPENSES
        // =====================================================

        BigDecimal totalExpenses =
                expenses.stream()
                        .filter(Objects::nonNull)
                        .map(Expense::getAmount)
                        .filter(Objects::nonNull)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        // =====================================================
        // 9. CALCULATE TRAVEL CHARGES
        // =====================================================

        BigDecimal totalTravel =
                travelCharges.stream()
                        .filter(Objects::nonNull)
                        .map(TravelCharge::getAmount)
                        .filter(Objects::nonNull)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        // =====================================================
        // 10. CALCULATE NET PAY
        //
        // Net Pay =
        // Gross Pay - Advances - Expenses + Travel
        // =====================================================

        BigDecimal netPay =
                grossPay
                        .subtract(totalAdvances)
                        .subtract(totalExpenses)
                        .add(totalTravel);

        // =====================================================
        // 11. CREATE REPORT SUMMARY
        // =====================================================

        ReportSummary summary =
                new ReportSummary();

        summary.setEmployeeId(
                employee.getId()
        );

        summary.setEmployeeName(
                employee.getName()
        );

        summary.setLocationName(
                employee.getLocation() != null
                        ? employee.getLocation().getName()
                        : "Unknown"
        );

        // Existing DTO field is totalHours,
        // but we are using it to display Number of Days.
        summary.setTotalHours(
                numberOfDays
        );

        // Overtime remains in HOURS.
        summary.setTotalOvertime(
                totalOvertimeHours
        );

        summary.setGrossPay(
                grossPay
        );

        summary.setAdvances(
                totalAdvances
        );

        summary.setExpenses(
                totalExpenses
        );

        summary.setTravelCharges(
                totalTravel
        );

        summary.setNetPay(
                netPay
        );

        return summary;
    }
}