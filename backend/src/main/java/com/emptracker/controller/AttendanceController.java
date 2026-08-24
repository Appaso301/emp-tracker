package com.emptracker.controller;

import com.emptracker.dto.AttendanceRequest;
import com.emptracker.model.AttendanceRecord;
import com.emptracker.service.AttendanceService;
import com.emptracker.service.EmployeeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4201", "http://localhost:4201", "http://127.0.0.1:4200", "http://localhost:65300", "http://127.0.0.1:65300","https://emp-tracker-eosin.vercel.app"})
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final EmployeeService employeeService;

    public AttendanceController(AttendanceService attendanceService, EmployeeService employeeService) {
        this.attendanceService = attendanceService;
        this.employeeService = employeeService;
    }

    @GetMapping("/employee/{employeeId}")
    public List<AttendanceRecord> getAttendanceForEmployee(
            @PathVariable Long employeeId,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        return attendanceService.findAttendanceForEmployee(employeeId, start, end);
    }

    @GetMapping("/location/{locationId}")
    public List<AttendanceRecord> getAttendanceForLocation(
            @PathVariable Long locationId,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        return attendanceService.findAttendanceForLocation(locationId, start, end);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importAttendanceWorkbook(@RequestParam("file") MultipartFile file) {
        try {
            int importCount = attendanceService.importAttendanceWorkbook(file);
            return ResponseEntity.ok().body(java.util.Map.of("rowsImported", importCount));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(java.util.Map.of("message", ex.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<AttendanceRecord> saveAttendance(@RequestBody AttendanceRequest request) {
        return employeeService.findById(request.getEmployeeId())
                .map(employee -> {
                    AttendanceRecord record = new AttendanceRecord();
                    record.setEmployee(employee);
                    record.setCheckIn(request.getCheckIn());
                    record.setCheckOut(request.getCheckOut());
                    if (request.getCheckIn() != null && request.getCheckOut() != null) {
                        long minutes = java.time.Duration.between(request.getCheckIn(), request.getCheckOut()).toMinutes();
                        double hours = minutes / 60.0;
                        record.setHoursWorked(hours);
                        record.setOvertimeHours(Math.max(0, hours - 8));
                        record.setWorkDate(request.getCheckIn().toLocalDate());
                    } else {
                        record.setHoursWorked(request.getHoursWorked());
                        record.setOvertimeHours(request.getOvertimeHours());
                        record.setWorkDate(LocalDate.now());
                    }
                    return ResponseEntity.ok(attendanceService.save(record));
                })
                .orElse(ResponseEntity.badRequest().build());
    }
}
