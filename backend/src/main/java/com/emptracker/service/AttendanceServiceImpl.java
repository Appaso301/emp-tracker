package com.emptracker.service;

import com.emptracker.model.AttendanceRecord;
import com.emptracker.model.Employee;
import com.emptracker.model.Location;
import com.emptracker.repository.AttendanceRecordRepository;
import com.emptracker.repository.EmployeeRepository;
import com.emptracker.repository.LocationRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRecordRepository repository;
    private final EmployeeRepository employeeRepository;
    private final LocationRepository locationRepository;

    public AttendanceServiceImpl(AttendanceRecordRepository repository,
                                 EmployeeRepository employeeRepository,
                                 LocationRepository locationRepository) {
        this.repository = repository;
        this.employeeRepository = employeeRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public AttendanceRecord save(AttendanceRecord attendanceRecord) {
        return repository.save(Objects.requireNonNull(attendanceRecord, "Attendance record must not be null"));
    }

    @Override
    public List<AttendanceRecord> findAttendanceForEmployee(Long employeeId, LocalDate start, LocalDate end) {
        return repository.findByEmployeeIdAndWorkDateBetween(
                Objects.requireNonNull(employeeId, "Employee id must not be null"),
                Objects.requireNonNull(start, "Start date must not be null"),
                Objects.requireNonNull(end, "End date must not be null"));
    }

    @Override
    public List<AttendanceRecord> findAttendanceForLocation(Long locationId, LocalDate start, LocalDate end) {
        return repository.findByEmployeeLocationIdAndWorkDateBetween(
                Objects.requireNonNull(locationId, "Location id must not be null"),
                Objects.requireNonNull(start, "Start date must not be null"),
                Objects.requireNonNull(end, "End date must not be null"));
    }

    @Override
    public int importAttendanceWorkbook(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("An Excel workbook must be selected for upload.");
        }

        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            if (sheet == null || sheet.getLastRowNum() < 1) {
                throw new IllegalArgumentException("The selected workbook does not contain any attendance rows.");
            }

            int rowsImported = 0;

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    continue;
                }

                String employeeName = readCell(row.getCell(0));
                String numberOfDaysText = readCell(row.getCell(1));
                String overtimeHoursText = readCell(row.getCell(2));
                String locationName = readCell(row.getCell(3));

                if (employeeName == null || employeeName.isBlank()) {
                    continue;
                }

                Optional<Employee> employee = employeeRepository.findByNameIgnoreCase(employeeName.trim());
                if (employee.isEmpty()) {
                    throw new IllegalArgumentException("Employee not found in the system: " + employeeName + " (row " + (rowIndex + 1) + ")");
                }

                double numberOfDays = parseDoubleValue(numberOfDaysText, "Number of Days", rowIndex + 1);
                double overtimeHours = parseDoubleValue(overtimeHoursText, "Over time hours", rowIndex + 1);

                if (locationName != null && !locationName.isBlank()) {
                    Optional<Location> location = locationRepository.findByNameIgnoreCase(locationName.trim());
                    if (location.isEmpty()) {
                        throw new IllegalArgumentException("Location not found in the system: " + locationName + " (row " + (rowIndex + 1) + ")");
                    }
                    if (employee.get().getLocation() != null && !employee.get().getLocation().getId().equals(location.get().getId())) {
                        throw new IllegalArgumentException("Employee " + employeeName + " is mapped to a different location in the system (row " + (rowIndex + 1) + ")");
                    }
                }

                AttendanceRecord attendanceRecord = new AttendanceRecord();
                attendanceRecord.setEmployee(employee.get());
                attendanceRecord.setCheckIn(LocalDateTime.now());
                attendanceRecord.setCheckOut(null);
                attendanceRecord.setWorkDate(LocalDate.now());
                attendanceRecord.setHoursWorked(numberOfDays);
                attendanceRecord.setOvertimeHours(overtimeHours);

                repository.save(attendanceRecord);
                rowsImported++;
            }

            return rowsImported;
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to read the uploaded Excel workbook.", e);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    private String readCell(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return FormulaEvaluatorUtil.evaluateFormula(cell);
            default:
                return null;
        }
    }

    private double parseDoubleValue(String value, String columnName, int rowNumber) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing value for " + columnName + " in row " + rowNumber);
        }

        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric value for " + columnName + " in row " + rowNumber + ": " + value, e);
        }
    }

    private static class FormulaEvaluatorUtil {
        private FormulaEvaluatorUtil() {
        }

        static String evaluateFormula(Cell cell) {
            return cell.toString();
        }
    }
}
