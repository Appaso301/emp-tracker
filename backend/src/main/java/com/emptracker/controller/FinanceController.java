package com.emptracker.controller;

import com.emptracker.dto.FinanceEntryRequest;
import com.emptracker.model.Advance;
import com.emptracker.model.Expense;
import com.emptracker.model.TravelCharge;
import com.emptracker.model.Employee;
import com.emptracker.repository.AdvanceRepository;
import com.emptracker.repository.ExpenseRepository;
import com.emptracker.repository.TravelChargeRepository;
import com.emptracker.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/finance")
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4201", "http://localhost:4201", "http://127.0.0.1:4200", "http://localhost:65300", "http://127.0.0.1:65300"})
public class FinanceController {

    private final EmployeeService employeeService;
    private final AdvanceRepository advanceRepository;
    private final ExpenseRepository expenseRepository;
    private final TravelChargeRepository travelChargeRepository;

    public FinanceController(EmployeeService employeeService,
                             AdvanceRepository advanceRepository,
                             ExpenseRepository expenseRepository,
                             TravelChargeRepository travelChargeRepository) {
        this.employeeService = employeeService;
        this.advanceRepository = advanceRepository;
        this.expenseRepository = expenseRepository;
        this.travelChargeRepository = travelChargeRepository;
    }

    @PostMapping("/advance")
    public ResponseEntity<Advance> saveAdvance(@RequestBody FinanceEntryRequest request) {
        return saveIfEmployeeExists(request.getEmployeeId(), employee -> {
            Advance advance = new Advance(employee, request.getAmount(), request.getDate(), request.getNote());
            return ResponseEntity.ok(advanceRepository.save(advance));
        });
    }

    @PostMapping("/expense")
    public ResponseEntity<Expense> saveExpense(@RequestBody FinanceEntryRequest request) {
        return saveIfEmployeeExists(request.getEmployeeId(), employee -> {
            Expense expense = new Expense(employee, request.getAmount(), request.getDate(), request.getCategory(), request.getNote());
            return ResponseEntity.ok(expenseRepository.save(expense));
        });
    }

    @PostMapping("/travel")
    public ResponseEntity<TravelCharge> saveTravel(@RequestBody FinanceEntryRequest request) {
        return saveIfEmployeeExists(request.getEmployeeId(), employee -> {
            TravelCharge travelCharge = new TravelCharge(employee, request.getAmount(), request.getDate(), request.getFromLocation(), request.getToLocation(), request.getNote());
            return ResponseEntity.ok(travelChargeRepository.save(travelCharge));
        });
    }

    private <T> ResponseEntity<T> saveIfEmployeeExists(Long employeeId, java.util.function.Function<Employee, ResponseEntity<T>> action) {
        return employeeService.findById(employeeId)
                .map(action)
                .orElse(ResponseEntity.badRequest().build());
    }
}
