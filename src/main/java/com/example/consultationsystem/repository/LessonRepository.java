package com.example.consultationsystem.repository;

import com.example.consultationsystem.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByProfessorIdAndStartTime(Integer professorId, Timestamp startTime);
    List<Lesson> findByRoomNumberAndStartTime(String roomNumber, Timestamp startTime);
    List<Lesson> findByProfessorIdAndStartTimeBetween(Integer professorId, Timestamp startTime, Timestamp startTime2);

    // Find lessons for a professor that clash with a given time slot (1-hour slots).
    @Query("SELECT l FROM Lesson l WHERE l.professorId = :professorId AND " +
            "(l.startTime = :proposedTime)")
    List<Lesson> findClashingLessonsByProfessorId(@Param("professorId") Integer professorId,
                                                  @Param("proposedTime") Timestamp proposedTim);

    // Find occupied rooms at the exact proposed start time OR if an existing slot ends at the proposed time.
    @Query("SELECT DISTINCT l.roomNumber FROM Lesson l WHERE " +
            "(l.startTime = :proposedTime OR l.startTime = :proposedTimeMinus1Hour)")
    List<String> findOccupiedRoomsAtTime(@Param("proposedTime") Timestamp proposedTime,
                                         @Param("proposedTimeMinus1Hour") Timestamp proposedTimeMinus1Hour);

}