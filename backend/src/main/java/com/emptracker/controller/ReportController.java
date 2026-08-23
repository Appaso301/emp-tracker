package com.emptracker.controller;

import com.emptracker.dto.ReportSummary;
import com.emptracker.service.ReportService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import com.emptracker.dto.LocationReportResponse;
@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4201", "http://localhost:4201", "http://127.0.0.1:4200", "http://localhost:65300", "http://127.0.0.1:65300"})
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/employee/{employeeId}")
    public List<ReportSummary> employeeReport(
            @PathVariable Long employeeId,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        return reportService.employeeReport(employeeId, start, end);
    }

    @GetMapping("/location/{locationId}")
    public LocationReportResponse locationReport(
            @PathVariable Long locationId,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {

        return reportService.locationReport(
                locationId,
                start,
                end
        );
    }

    @GetMapping("/consolidated")
    public ReportSummary consolidatedReport(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        return reportService.consolidatedReport(start, end);
    }
}
