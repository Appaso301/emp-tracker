package com.emptracker.repository;

import com.emptracker.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByLocationId(Long locationId);
    Optional<Employee> findByNameIgnoreCase(String name);
}
