package com.emptracker.service;

import com.emptracker.model.AttendanceRecord;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
    AttendanceRecord save(AttendanceRecord attendanceRecord);
    List<AttendanceRecord> findAttendanceForEmployee(Long employeeId, LocalDate start, LocalDate end);
    List<AttendanceRecord> findAttendanceForLocation(Long locationId, LocalDate start, LocalDate end);
    int importAttendanceWorkbook(MultipartFile file);
}
