package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "chitieungay")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_khoan;

    @ManyToOne
    @JoinColumn(name = "id_user")
    @JsonBackReference("user-expense")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_label")
    @JsonBackReference("label-expense")
    private Label label;

    private Double so_tien;
    private String note;
    private String time_use;

    // Getters and setters
    public Long getId_khoan() {
        return id_khoan;
    }

    public void setId_khoan(Long id_khoan) {
        this.id_khoan = id_khoan;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public Double getSo_tien() {
        return so_tien;
    }

    public void setSo_tien(Double so_tien) {
        this.so_tien = so_tien;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTime_use() {
        return time_use;
    }

    public void setTime_use(String time_use) {
        this.time_use = time_use;
    }
}