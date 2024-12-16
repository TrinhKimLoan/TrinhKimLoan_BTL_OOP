package com.example.demo.repositories;

import com.example.demo.models.Expense;
import com.example.demo.models.Label;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserAndLabel(User user, Label label);
    @Query("SELECT l.name AS labelName, SUM(e.so_tien) AS totalSpent " +
    "FROM Expense e " +
    "JOIN e.label l " +
    "WHERE e.user.id = :userId AND DATE_FORMAT(e.time_use, '%Y-%m') = :monthYear " +
    "GROUP BY l.name")
List<Object[]> findMonthlyExpenseReport(@Param("userId") Long userId, @Param("monthYear") String monthYear);
}