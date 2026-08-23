package com.emptracker.service;

import com.emptracker.dto.EmployeeCreateRequest;
import com.emptracker.model.Employee;
import com.emptracker.model.Location;
import com.emptracker.repository.EmployeeRepository;
import com.emptracker.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final LocationRepository locationRepository;

    public EmployeeServiceImpl(
            EmployeeRepository employeeRepository,
            LocationRepository locationRepository) {

        this.employeeRepository = employeeRepository;
        this.locationRepository = locationRepository;
    }

    // =========================================================
    // GET ALL EMPLOYEES
    // =========================================================

    @Override
    public List<Employee> listAllEmployees() {
        return employeeRepository.findAll();
    }

    // =========================================================
    // GET EMPLOYEE BY ID
    // =========================================================

    @Override
    public Optional<Employee> findById(Long id) {

        return employeeRepository.findById(
                Objects.requireNonNull(
                        id,
                        "Employee id must not be null"
                )
        );
    }

    // =========================================================
    // SAVE EMPLOYEE
    // =========================================================

    @Override
    public Employee save(Employee employee) {

        return employeeRepository.save(
                Objects.requireNonNull(
                        employee,
                        "Employee must not be null"
                )
        );
    }

    // =========================================================
    // CREATE EMPLOYEE
    // =========================================================

    @Override
    public Employee createEmployee(EmployeeCreateRequest request) {

        Objects.requireNonNull(
                request,
                "Employee creation request must not be null"
        );

        Objects.requireNonNull(
                request.getName(),
                "Employee name must not be null"
        );

        Objects.requireNonNull(
                request.getLocationId(),
                "Location id must not be null"
        );

        Objects.requireNonNull(
                request.getDailyRate(),
                "Daily rate must not be null"
        );

        Objects.requireNonNull(
                request.getHourlyRate(),
                "Hourly rate must not be null"
        );

        Objects.requireNonNull(
                request.getOvertimeRate(),
                "Overtime rate must not be null"
        );

        Location location =
                locationRepository.findById(
                        request.getLocationId()
                ).orElseThrow(
                        () -> new IllegalArgumentException(
                                "Location not found: "
                                        + request.getLocationId()
                        )
                );

        Employee employee = new Employee(
                request.getName(),
                request.getMobile(),
                location,
                request.getDailyRate(),
                request.getHourlyRate(),
                request.getOvertimeRate()
        );

        employee.setActive(
                Boolean.TRUE.equals(request.getActive())
                        || request.getActive() == null
        );

        return employeeRepository.save(employee);
    }

    // =========================================================
    // UPDATE EMPLOYEE
    // =========================================================

    @Override
    public Employee updateEmployee(
            Long id,
            EmployeeCreateRequest request) {

        Objects.requireNonNull(
                id,
                "Employee id must not be null"
        );

        Objects.requireNonNull(
                request,
                "Employee update request must not be null"
        );

        // Find existing employee
        Employee employee =
                employeeRepository.findById(id)
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "Employee not found: " + id
                                )
                        );

        // -----------------------------------------------------
        // Employee Name
        // -----------------------------------------------------

        if (request.getName() != null) {
            employee.setName(request.getName());
        }

        // -----------------------------------------------------
        // Mobile
        // -----------------------------------------------------

        employee.setMobile(request.getMobile());

        // -----------------------------------------------------
        // Location
        // -----------------------------------------------------

        if (request.getLocationId() != null) {

            Location location =
                    locationRepository.findById(
                            request.getLocationId()
                    ).orElseThrow(
                            () -> new IllegalArgumentException(
                                    "Location not found: "
                                            + request.getLocationId()
                            )
                    );

            employee.setLocation(location);
        }

        // -----------------------------------------------------
        // Daily Rate
        // -----------------------------------------------------

        if (request.getDailyRate() != null) {
            employee.setDailyRate(
                    request.getDailyRate()
            );
        }

        // -----------------------------------------------------
        // Hourly Rate
        // -----------------------------------------------------

        if (request.getHourlyRate() != null) {
            employee.setHourlyRate(
                    request.getHourlyRate()
            );
        }

        // -----------------------------------------------------
        // Overtime Rate
        // -----------------------------------------------------

        if (request.getOvertimeRate() != null) {
            employee.setOvertimeRate(
                    request.getOvertimeRate()
            );
        }

        // -----------------------------------------------------
        // Active
        // -----------------------------------------------------

        if (request.getActive() != null) {
            employee.setActive(
                    request.getActive()
            );
        }

        return employeeRepository.save(employee);
    }

    // =========================================================
    // DELETE EMPLOYEE
    // =========================================================

    @Override
    public void deleteEmployee(Long id) {

        Objects.requireNonNull(
                id,
                "Employee id must not be null"
        );

        Employee employee =
                employeeRepository.findById(id)
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "Employee not found: " + id
                                )
                        );

        employeeRepository.delete(employee);
    }

    // =========================================================
    // LOCATIONS
    // =========================================================

    @Override
    public List<Location> listAllLocations() {
        return locationRepository.findAll();
    }

    @Override
    public Optional<Location> findLocationById(Long id) {

        return locationRepository.findById(
                Objects.requireNonNull(
                        id,
                        "Location id must not be null"
                )
        );
    }

    @Override
    public Location saveLocation(Location location) {

        return locationRepository.save(
                Objects.requireNonNull(
                        location,
                        "Location must not be null"
                )
        );
    }
}