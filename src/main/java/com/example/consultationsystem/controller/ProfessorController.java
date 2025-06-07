package com.example.consultationsystem.controller;

import com.example.consultationsystem.entity.Consultation;
import com.example.consultationsystem.entity.Lesson;
import com.example.consultationsystem.entity.Professor;
import com.example.consultationsystem.exception.NoProfessorsException;
import com.example.consultationsystem.service.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/professors")
public class ProfessorController {

    @Autowired
    private ConsultationService consultationService;

    @GetMapping()
    public ResponseEntity<List<Professor>> getAllProfessors() {
        try {
            List<Professor> professors = consultationService.getAllProfessors();
            return ResponseEntity.ok(professors);

        } catch (NoProfessorsException e){
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }

    }

    @GetMapping("/{id}/lessons")
    public ResponseEntity<List<Lesson>> getProfessorsLessonsById(
            @PathVariable Integer id,
            @RequestParam(value = "weekOffset", defaultValue = "0") Integer weekOffset) {
        try {
            List<Lesson> lessons = consultationService.getLessonsByProfessorAndWeek(id, weekOffset);
            if (lessons.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lessons);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}/consultations")
    public ResponseEntity<List<Consultation>> getProfessorsConsultationsById(
            @PathVariable Integer id,
            @RequestParam(value = "weekOffset", defaultValue = "0") Integer weekOffset) {
        try {
            List<Consultation> consultations = consultationService.getConsultationsByProfessorAndWeek(id, weekOffset);
            if (consultations.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(consultations);
        } catch (Exception e) {

            return ResponseEntity.internalServerError().build();
        }
    }
}
