package com.example.consultationsystem.controller;

import com.example.consultationsystem.entity.Professor;
import com.example.consultationsystem.entity.Student;
import com.example.consultationsystem.exception.NoProfessorsException;
import com.example.consultationsystem.exception.NoStudentFoundException;
import com.example.consultationsystem.service.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    @Autowired
    private ConsultationService consultationService;

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Integer id) {
        try {
            Student student = consultationService.getStudentById(id);
            return ResponseEntity.ok(student);

        } catch (NoStudentFoundException e){
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }

    }
}
