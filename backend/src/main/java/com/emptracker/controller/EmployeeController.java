package com.emptracker.controller;

import com.emptracker.dto.EmployeeCreateRequest;
import com.emptracker.model.Employee;
import com.emptracker.model.Location;
import com.emptracker.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(
        origins = {
                "http://localhost:4200",
                "http://127.0.0.1:4201",
                "http://localhost:4201",
                "http://127.0.0.1:4200",
                "http://localhost:65300",
                "http://127.0.0.1:65300",
                "https://emp-tracker-eosin.vercel.app"
        }
)
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(
            EmployeeService employeeService) {

        this.employeeService = employeeService;
    }

    // =========================================================
    // GET ALL EMPLOYEES
    // =========================================================

    @GetMapping
    public List<Employee> getAllEmployees() {

        return employeeService.listAllEmployees();
    }

    // =========================================================
    // GET LOCATIONS
    // =========================================================

    @GetMapping("/locations")
    public List<Location> getLocations() {

        return employeeService.listAllLocations();
    }

    // =========================================================
    // GET EMPLOYEE BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(
            @PathVariable Long id) {

        return employeeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(
                        ResponseEntity.notFound().build()
                );
    }

    // =========================================================
    // CREATE EMPLOYEE
    // =========================================================

    @PostMapping
    public ResponseEntity<Employee> createEmployee(
            @RequestBody EmployeeCreateRequest request) {

        try {

            return ResponseEntity.ok(
                    employeeService.createEmployee(request)
            );

        } catch (IllegalArgumentException ex) {

            return ResponseEntity.badRequest().build();
        }
    }

    // =========================================================
    // UPDATE EMPLOYEE
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @RequestBody EmployeeCreateRequest request) {

        try {

            Employee updatedEmployee =
                    employeeService.updateEmployee(
                            id,
                            request
                    );

            return ResponseEntity.ok(
                    updatedEmployee
            );

        } catch (IllegalArgumentException ex) {

            return ResponseEntity.badRequest().build();
        }
    }

    // =========================================================
    // DELETE EMPLOYEE
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable Long id) {

        try {

            employeeService.deleteEmployee(id);

            return ResponseEntity.noContent().build();

        } catch (IllegalArgumentException ex) {

            return ResponseEntity.notFound().build();
        }
    }

    // =========================================================
    // CREATE LOCATION
    // =========================================================

    @PostMapping("/locations")
    public Location createLocation(
            @RequestBody Location location) {

        return employeeService.saveLocation(location);
    }
}