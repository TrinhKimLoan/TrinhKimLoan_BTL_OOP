package com.example.demo.controllers;

import com.example.demo.models.Expense;
import com.example.demo.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.exceptions.ResourceNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/users/{id_user}/labels/{id_label}/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping
    public List<Expense> getAllExpenses(@PathVariable Long id_user, @PathVariable Long id_label) {
        return expenseService.findAllByUserAndLabel(id_user, id_label);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Long id) {
        Expense expense = expenseService.findById(id);
        if (expense != null) {
            return ResponseEntity.ok(expense);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Expense> addExpense(@PathVariable Long id_user, @PathVariable Long id_label, @RequestBody Expense expense) {
        try {
            Expense createdExpense = expenseService.save(id_user, id_label, expense);
            return ResponseEntity.ok(createdExpense);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long id, @RequestBody Expense expenseDetails) {
        Expense updatedExpense = expenseService.update(id, expenseDetails);
        if (updatedExpense != null) {
            return ResponseEntity.ok(updatedExpense);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        if (expenseService.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    
}
