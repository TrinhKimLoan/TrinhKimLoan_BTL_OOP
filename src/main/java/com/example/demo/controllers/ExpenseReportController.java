package com.example.demo.controllers;

import com.example.demo.services.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.ExpenseReportDTO;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseReportController {

    private final ExpenseService expenseService;

    public ExpenseReportController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("/monthly-report")
    public ResponseEntity<List<ExpenseReportDTO>> getMonthlyReport(
            @RequestParam Long userId,
            @RequestParam int month,
            @RequestParam int year) {

        List<ExpenseReportDTO> report = expenseService.getMonthlyReport(userId, month, year);
        return ResponseEntity.ok(report);
    }
}

