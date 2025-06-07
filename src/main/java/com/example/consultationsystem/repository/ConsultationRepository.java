package com.example.consultationsystem.repository;

import com.example.consultationsystem.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Integer> {
    List<Consultation> findBysNumber(Integer sNumber);
    List<Consultation> findByProfessorIdAndStartTime(Integer professorId, Timestamp startTime);
    List<Consultation> findByRoomNumberAndStartTime(String roomNumber, Timestamp startTime);

    List<Consultation> findByRoomNumberAndStartTimeBetween(String roomNumber, Timestamp start, Timestamp end);

    List<Consultation> findBysNumberAndStartTimeBetween(Integer sNumber, Timestamp start, Timestamp end);


    List<Consultation> findAllByRoomNumber(String roomNumber);

    List<Consultation> findByProfessorIdAndStartTimeBetween(Integer professorId, Timestamp startTime, Timestamp startTime2);

    @Query("SELECT c FROM Consultation c WHERE c.sNumber = :sNumber AND c.professorId = :professorId AND c.startTime >= :currentTime")
    List<Consultation> findActiveConsultationsBysNumberAndProfessorId(@Param("sNumber") Integer sNumber, @Param("professorId") Integer professorId, @Param("currentTime") Timestamp currentTime);


    @Query("SELECT c FROM Consultation c WHERE c.professorId = :professorId AND " +
            "(c.startTime = :proposedTime)")
    List<Consultation> findClashingConsultationsByProfessorId(@Param("professorId") Integer professorId,
                                                              @Param("proposedTime") Timestamp proposedTime);

    @Query("SELECT DISTINCT c.roomNumber FROM Consultation c WHERE " +
            "(c.startTime = :proposedTime OR c.startTime = :proposedTimeMinus1Hour)")
    List<String> findOccupiedRoomsAtTime(@Param("proposedTime") Timestamp proposedTime,
                                         @Param("proposedTimeMinus1Hour") Timestamp proposedTimeMinus1Hour);

    @Query("SELECT c FROM Consultation c WHERE c.sNumber = :sNumber AND " +
            "(c.startTime = :proposedTime)")
    List<Consultation> findClashingConsultationsBySNumber(@Param("sNumber") Integer sNumber,
                                                          @Param("proposedTime") Timestamp proposedTime);
}