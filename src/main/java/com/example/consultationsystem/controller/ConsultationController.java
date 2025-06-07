package com.example.consultationsystem.controller;

import com.example.consultationsystem.dto.ConsultationRequest;
import com.example.consultationsystem.dto.RescheduleRequest;
import com.example.consultationsystem.entity.Consultation;
import com.example.consultationsystem.exception.ConsultationException;
import com.example.consultationsystem.service.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultations")
public class ConsultationController {

    @Autowired
    private ConsultationService consultationService;

    @GetMapping("/{sNumber}")
    public ResponseEntity<List<Consultation>> getConsultationsByStudent(
            @PathVariable Integer sNumber,
            @RequestParam(value = "weekOffset", defaultValue = "0") Integer weekOffset) {
        List<Consultation> consultations = consultationService.getAllConsultationsByStudent(sNumber, weekOffset);
        return ResponseEntity.ok(consultations);
    }

    @PostMapping
    public ResponseEntity<Consultation> registerConsultation(@RequestBody ConsultationRequest request) {
        try {
            Consultation consultation = consultationService.registerConsultation(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(consultation);
        } catch (ConsultationException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{consultationId}")
    public ResponseEntity<Consultation> rescheduleConsultation(
            @PathVariable Integer consultationId,
            @RequestBody RescheduleRequest request
    ) {
        try {
            Consultation updated = consultationService.rescheduleConsultation(consultationId, request.getNewStartTime());
            return ResponseEntity.ok(updated);
        } catch (ConsultationException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{consultationId}")
    public ResponseEntity<Void> deleteConsultation(@PathVariable Integer consultationId) {
        try {
            consultationService.deleteConsultation(consultationId);
            return ResponseEntity.noContent().build();
        } catch (ConsultationException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
