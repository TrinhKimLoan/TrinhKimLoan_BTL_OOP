package com.example.demo.services;

import com.example.demo.dto.ExpenseReportDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Expense;
import com.example.demo.models.Label;
import com.example.demo.models.User;
import com.example.demo.repositories.ExpenseRepository;
import com.example.demo.repositories.LabelRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    public List<Expense> findAll() {
        return expenseRepository.findAll();
    }

    public List<Expense> findAllByUserAndLabel(Long id_user, Long id_label) {
        User user = userRepository.findById(id_user).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Label label = labelRepository.findById(id_label).orElseThrow(() -> new ResourceNotFoundException("Label not found"));
        return expenseRepository.findByUserAndLabel(user, label);
    }

    public Expense findById(Long id) {
        return expenseRepository.findById(id).orElse(null);
    }

    public Expense save(Long id_user, Long id_label, Expense expense) {
        User user = userRepository.findById(id_user).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Label label = labelRepository.findById(id_label).orElseThrow(() -> new ResourceNotFoundException("Label not found"));
        expense.setUser(user);
        expense.setLabel(label);
        return expenseRepository.save(expense);
    }

    public Expense update(Long id, Expense expenseDetails) {
        return expenseRepository.findById(id).map(expense -> {
            expense.setSo_tien(expenseDetails.getSo_tien());
            expense.setNote(expenseDetails.getNote());
            expense.setTime_use(expenseDetails.getTime_use());
            return expenseRepository.save(expense);
        }).orElse(null);
    }

    public boolean delete(Long id) {
        return expenseRepository.findById(id).map(expense -> {
            expenseRepository.delete(expense);
            return true;
        }).orElse(false);
    }

    public List<ExpenseReportDTO> getMonthlyExpenseReport(Long userId, String monthYear) {
        List<Object[]> results = expenseRepository.findMonthlyExpenseReport(userId, monthYear);
        
        return results.stream()
            .map(result -> new ExpenseReportDTO(
                (String) result[0], // labelName
                (Double) result[1]  // totalSpent
            ))
            .collect(Collectors.toList());
    }
    
    public List<ExpenseReportDTO> getMonthlyReport(Long userId, int month, int year) {
        // Chuyển đổi month và year thành chuỗi "YYYY-MM"
        String monthYear = String.format("%04d-%02d", year, month);
    
        // Gọi phương thức đã có trong repository
        List<Object[]> results = expenseRepository.findMonthlyExpenseReport(userId, monthYear);
    
        // Chuyển đổi kết quả sang danh sách DTO
        return results.stream()
                .map(result -> new ExpenseReportDTO(
                    (String) result[0], // labelName
                    (Double) result[1]  // totalSpent
                ))
                .collect(Collectors.toList());
    }
    
}