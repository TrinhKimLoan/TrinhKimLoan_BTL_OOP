package com.example.demo.dto;

public class ExpenseReportDTO {
    private String labelName;
    private Double totalSpent;

    // Constructors
    public ExpenseReportDTO(String labelName, Double totalSpent) {
        this.labelName = labelName;
        this.totalSpent = totalSpent;
    }

    // Getters and Setters
    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public Double getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(Double totalSpent) {
        this.totalSpent = totalSpent;
    }
}

