package com.example.consultationsystem.repository;


import com.example.consultationsystem.entity.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RepositoryTests {

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

    private Professor professor;
    private Room room;
    private Student student;

    @BeforeAll
    void clearTable() {
        professorRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        // Create sample data
        professor = new Professor();
        professor.setFirstName("John");
        professor.setLastName("Doe");
        professorRepository.save(professor);


        room = new Room();
        room.setNumber("101");
        roomRepository.save(room);

        student = new Student();
        student.setSNumber(1001);
        student.setFirstName("Alice");
        student.setLastName("Smith");
        Optional<Student> existingStudent = studentRepository.findById(1);
        if (existingStudent.isEmpty()) {
            studentRepository.save(student);
        }
    }

    @Test
    void testSaveAndFindConsultation() {
        LocalDate today = LocalDate.now();
        LocalDateTime startDateTime = today.atTime(17, 0);  // 17:00
        Timestamp startTime = Timestamp.valueOf(startDateTime);

        Consultation consultation = new Consultation();
        consultation.setProfessorId(professor.getId());
        consultation.setRoomNumber(room.getNumber());
        consultation.setsNumber(student.getSNumber());
        consultation.setStartTime(startTime);

        var saved = consultationRepository.save(consultation);

        Optional<Consultation> found = consultationRepository.findById(saved.getId());
        assertThat(found).isPresent();
    }

    @Test
    void testFindConsultationByStudentNumber() {
        Consultation consultation = new Consultation();
        //consultation.setId(2);
        consultation.setProfessorId(professor.getId());
        consultation.setRoomNumber(room.getNumber());
        consultation.setsNumber(student.getSNumber());
        consultation.setStartTime(Timestamp.from(Instant.now()));

        consultationRepository.save(consultation);

        List<Consultation> consultations = consultationRepository.findBysNumber(1001);
        assertThat(consultations).isNotEmpty();
    }

    @Test
    void testFindConsultationByRoomAndTime() {
        Timestamp startTime = Timestamp.from(Instant.now());

        Consultation consultation = new Consultation();
        consultation.setProfessorId(professor.getId());
        consultation.setRoomNumber(room.getNumber());
        consultation.setsNumber(student.getSNumber());
        consultation.setStartTime(startTime);

        var saved = consultationRepository.save(consultation);

        // Define a range ±1 second around startTime
        Timestamp startRange = new Timestamp(startTime.getTime() - 1000);
        Timestamp endRange = new Timestamp(startTime.getTime() + 1000);

        List<Consultation> found = consultationRepository.findByRoomNumberAndStartTimeBetween(saved.getRoomNumber(), startRange, endRange);

        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getId()).isEqualTo(saved.getId());
    }


    @Test
    void testSaveAndFindLesson() {
        // Set startTime to today at 17:00
        LocalDate today = LocalDate.now();
        LocalDateTime startDateTime = today.atTime(17, 0);  // 17:00
        Timestamp startTime = Timestamp.valueOf(startDateTime);

        Lesson lesson = new Lesson();
        lesson.setProfessorId(professor.getId());
        lesson.setRoomNumber(room.getNumber());
        lesson.setStartTime(startTime);
        lesson.setEndTime(Timestamp.from(startTime.toInstant().plusSeconds(3600))); // 1-hour lesson

        lessonRepository.save(lesson);

        List<Lesson> lessons = lessonRepository.findByRoomNumberAndStartTime(room.getNumber(), lesson.getStartTime());
        assertThat(lessons).isNotEmpty();
    }


    @Test
    void testFindRoom() {
        Optional<Room> found = roomRepository.findById("101");
        assertThat(found).isPresent();
        assertThat(found.get().getNumber()).isEqualTo("101");
    }

    @Test
    void testFindProfessor() {
        List<Professor> professors = professorRepository.findAll();

        // Find professor with firstName "John"
        Optional<Professor> found = professors.stream()
                .filter(p -> "John".equals(p.getFirstName()))
                .findFirst();

        assertThat(found).isPresent();
    }


    @Test
    void testSavetudent() {
        // Use saveAndFlush to ensure it’s persisted
        Student student1 = new Student();
        student1.setFirstName("Test ");
        student1.setLastName("Prof");
        student1.setSNumber(123);
        Student saved = studentRepository.saveAndFlush(student1);


        assertNotNull(123);
    }

    @Test
    void testFindStudent() {
        Optional<Student> found = studentRepository.findById(1001);
        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("Alice");
    }

    @Test
    @Transactional
    void testSaveProfessor() {
        // Use saveAndFlush to ensure it’s persisted
        Professor professor = new Professor();
        professor.setFirstName("Test ");
        professor.setLastName("Prof");
        Professor saved = professorRepository.saveAndFlush(professor);

        // Avoid manually setting ID
        assertNotNull(saved.getId());
    }

}
