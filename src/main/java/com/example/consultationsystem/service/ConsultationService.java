package com.example.consultationsystem.service;

import com.example.consultationsystem.dto.ConsultationRequest;
import com.example.consultationsystem.entity.*;
import com.example.consultationsystem.exception.ConsultationException;
import com.example.consultationsystem.exception.NoProfessorsException;
import com.example.consultationsystem.exception.NoStudentFoundException;
import com.example.consultationsystem.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class ConsultationService {

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private StudentRepository studentRepository;

    public List<Lesson> getLessonsByProfessorAndWeek(Integer professorId, Integer weekOffset) {
        LocalDateTime now = LocalDateTime.now();


        LocalDateTime startOfCurrentWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .withHour(0).withMinute(0).withSecond(0).withNano(0);


        LocalDateTime startOfWeek = startOfCurrentWeek.plusWeeks(weekOffset);
        LocalDateTime endOfWeek = startOfWeek.plusWeeks(1).minusNanos(1);

        Timestamp startTimestamp = Timestamp.valueOf(startOfWeek);
        Timestamp endTimestamp = Timestamp.valueOf(endOfWeek);

        return lessonRepository.findByProfessorIdAndStartTimeBetween(professorId, startTimestamp, endTimestamp);
    }

    public List<Consultation> getAllConsultationsByStudent(Integer sNumber, int weekOffset) {

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime startOfWeek = now.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1).withHour(0).withMinute(0).withSecond(0).withNano(0);

        startOfWeek = startOfWeek.plusWeeks(weekOffset);
        LocalDateTime endOfWeek = startOfWeek.plusDays(4).withHour(23).withMinute(59).withSecond(59);


        Timestamp startTimestamp = Timestamp.valueOf(startOfWeek);
        Timestamp endTimestamp = Timestamp.valueOf(endOfWeek);


        System.out.println("Fetching consultations for sNumber=" + sNumber + " from " + startTimestamp + " to " + endTimestamp);


        return consultationRepository.findBysNumberAndStartTimeBetween(sNumber, startTimestamp, endTimestamp);
    }

    @Transactional
    public Consultation registerConsultation(ConsultationRequest request) {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        if (request.getStartTime().before(currentTime)) {
            throw new ConsultationException("Cannot register for a past time.");
        }

        List<Consultation> activeStudentConsultations = consultationRepository
                .findActiveConsultationsBysNumberAndProfessorId(
                        request.getsNumber(),
                        request.getProfessorId(),
                        currentTime
                );

        if (!activeStudentConsultations.isEmpty()) {
            throw new ConsultationException("Student already has an active consultation with this professor.");
        }

        if (!isProfessorAvailable(request.getProfessorId(), request.getStartTime())) {
            throw new ConsultationException("Professor is unavailable at this time. Please choose another slot.");
        }

        List<Consultation> clashingStudentConsultations = consultationRepository
                .findClashingConsultationsBySNumber(
                        request.getsNumber(),
                        request.getStartTime()
                );

        if (!clashingStudentConsultations.isEmpty()) {
            throw new ConsultationException("Student is already booked for another consultation at this time.");
        }

        String availableRoom = findAvailableRoom(request.getStartTime());
        if (availableRoom == null) {
            throw new ConsultationException("No available rooms at this time.");
        }

        Consultation consultation = new Consultation();
        consultation.setsNumber(request.getsNumber());
        consultation.setProfessorId(request.getProfessorId());
        consultation.setRoomNumber(availableRoom);
        consultation.setStartTime(request.getStartTime());

        return consultationRepository.save(consultation);
    }

    @Transactional
    public Consultation rescheduleConsultation(Integer consultationId, Timestamp newStartTime) {
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new ConsultationException("Consultation not found."));

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());


        if (newStartTime.before(currentTime)) {
            throw new ConsultationException("Cannot reschedule to a past time.");
        }


        if (consultation.getStartTime().before(currentTime)) {
            throw new ConsultationException("Cannot reschedule a consultation that has already started or completed.");
        }

        List<Consultation> clashingStudentConsultations = consultationRepository
                .findClashingConsultationsBySNumber(
                        consultation.getsNumber(),
                        newStartTime
                );

        boolean studentHasOtherClashingConsultation = clashingStudentConsultations.stream()
                .anyMatch(c -> !c.getId().equals(consultationId));

        if (studentHasOtherClashingConsultation) {
            throw new ConsultationException("Student is already booked for another consultation at the requested new time.");
        }


        if (!isProfessorAvailable(consultation.getProfessorId(), newStartTime)) {
            throw new ConsultationException("The professor is unavailable at the requested new time. Please choose another slot.");
        }

        String availableRoom = findAvailableRoom(newStartTime);
        if (availableRoom == null) {
            throw new ConsultationException("No available rooms at the requested new time.");
        }

        consultation.setStartTime(newStartTime);
        consultation.setRoomNumber(availableRoom);

        return consultationRepository.save(consultation);
    }

    private boolean isProfessorAvailable(Integer professorId, Timestamp proposedStartTime) {

        List<Lesson> clashingLessons = lessonRepository.findClashingLessonsByProfessorId(
                professorId, proposedStartTime);
        if (!clashingLessons.isEmpty()) {
            return false;
        }

        List<Consultation> clashingConsultations = consultationRepository.findClashingConsultationsByProfessorId(
                professorId, proposedStartTime);
        if (!clashingConsultations.isEmpty()) {
            return false;
        }

        return true;
    }

    public void deleteConsultation(Integer consultationId) {
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new ConsultationException("Consultation not found."));
        System.out.println(consultation.getId());
        consultationRepository.deleteById(consultation.getId());
    }


    private String findAvailableRoom(Timestamp startTime) {
        List<Room> rooms = roomRepository.findAll();
        for (Room room : rooms) {
            List<Lesson> lessons = lessonRepository.findByRoomNumberAndStartTime(room.getNumber(), startTime);
            if (!lessons.isEmpty()) {
                continue;
            }
            List<Consultation> consultations = consultationRepository.findByRoomNumberAndStartTime(room.getNumber(), startTime);
            if (consultations.isEmpty()) {
                return room.getNumber();
            }
        }
        return null;
    }

    public List<Professor> getAllProfessors() {
        List<Professor> professors = professorRepository.findAll();
        if (professors.isEmpty()) throw new NoProfessorsException();
        return professors;
    }

    public Student getStudentById(Integer id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isEmpty()) throw new NoStudentFoundException();
        return student.get();
    }

    public List<Consultation> getConsultationsByProfessorAndWeek(Integer professorId, Integer weekOffset) {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime startOfCurrentWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .withHour(0).withMinute(0).withSecond(0).withNano(0);

        LocalDateTime startOfWeek = startOfCurrentWeek.plusWeeks(weekOffset);
        LocalDateTime endOfWeek = startOfWeek.plusWeeks(1).minusNanos(1);

        Timestamp startTimestamp = Timestamp.valueOf(startOfWeek);
        Timestamp endTimestamp = Timestamp.valueOf(endOfWeek);

        return consultationRepository.findByProfessorIdAndStartTimeBetween(professorId, startTimestamp, endTimestamp);
    }
}
