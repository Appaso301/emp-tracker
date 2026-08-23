package com.emptracker.service;

import com.emptracker.dto.EmployeeCreateRequest;
import com.emptracker.model.Employee;
import com.emptracker.model.Location;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    List<Employee> listAllEmployees();

    Optional<Employee> findById(Long id);

    Employee save(Employee employee);

    Employee createEmployee(EmployeeCreateRequest request);

    Employee updateEmployee(Long id, EmployeeCreateRequest request);

    void deleteEmployee(Long id);

    List<Location> listAllLocations();

    Optional<Location> findLocationById(Long id);

    Location saveLocation(Location location);
}