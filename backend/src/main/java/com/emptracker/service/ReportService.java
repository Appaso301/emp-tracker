package com.emptracker.service;

import com.emptracker.dto.ReportSummary;
import com.emptracker.dto.LocationReportResponse;
import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    List<ReportSummary> employeeReport(Long employeeId, LocalDate start, LocalDate end);
    LocationReportResponse locationReport(Long locationId,LocalDate start,LocalDate end);
    ReportSummary consolidatedReport(LocalDate start, LocalDate end);
}
