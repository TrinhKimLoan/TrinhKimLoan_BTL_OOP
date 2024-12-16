package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "labels")
public class Label {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_label;

    @ManyToOne
    @JoinColumn(name = "id_user")
    @JsonBackReference("user-label")
    private User user;

    private String name;

    @OneToMany(mappedBy = "label", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("label-expense")
    private List<Expense> expenses;

    // Getters and setters
    public Long getId_label() {
        return id_label;
    }

    public void setId_label(Long id_label) {
        this.id_label = id_label;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }
}