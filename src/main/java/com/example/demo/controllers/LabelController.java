package com.example.demo.controllers;


import com.example.demo.models.Label;
import com.example.demo.services.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/users/{id_user}/labels")
public class LabelController {


    @Autowired
    private LabelService labelService;


    @GetMapping
    public List<Label> getAllLabels(@PathVariable Long id_user) {
        return labelService.findAllByUserId(id_user);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Label> getLabelById(@PathVariable Long id) {
        Label label = labelService.findById(id);
        if (label != null) {
            return ResponseEntity.ok(label);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping
    public Label createLabel(@PathVariable Long id_user, @RequestBody Label label) {
        return labelService.save(id_user, label);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Label> updateLabel(@PathVariable Long id, @RequestBody Label labelDetails) {
        Label updatedLabel = labelService.update(id, labelDetails);
        if (updatedLabel != null) {
            return ResponseEntity.ok(updatedLabel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLabel(@PathVariable Long id) {
        if (labelService.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
