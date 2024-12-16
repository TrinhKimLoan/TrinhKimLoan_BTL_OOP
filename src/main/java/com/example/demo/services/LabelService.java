package com.example.demo.services;

import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Label;
import com.example.demo.models.User;
import com.example.demo.repositories.LabelRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {


    @Autowired
    private LabelRepository labelRepository;

    public List<Label> findAllByUserId(Long userId) {
        return labelRepository.findByUserId(userId);
    }


    @Autowired
    private UserRepository userRepository;


    public List<Label> findAll() {
        return labelRepository.findAll();
    }


    public Label findById(Long id) {
        return labelRepository.findById(id).orElse(null);
    }


    public Label save(Long id_user, Label label) {
        User user = userRepository.findById(id_user)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        label.setUser(user);
        return labelRepository.save(label);
    }


    public Label update(Long id, Label labelDetails) {
        return labelRepository.findById(id).map(label -> {
            label.setName(labelDetails.getName());
            return labelRepository.save(label);
        }).orElse(null);
    }


    public boolean delete(Long id) {
        return labelRepository.findById(id).map(label -> {
            labelRepository.delete(label);
            return true;
        }).orElse(false);
    }
}
