package com.emptracker.repository;

import com.emptracker.model.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
    List<AttendanceRecord> findByEmployeeIdAndWorkDateBetween(Long employeeId, LocalDate start, LocalDate end);
    List<AttendanceRecord> findByEmployeeLocationIdAndWorkDateBetween(Long locationId, LocalDate start, LocalDate end);
}
