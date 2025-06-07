package com.example.consultationsystem.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@IdClass(LessonId.class)
public class Lesson {

    @Version
    private Integer version;

    @Id
    @Column(name = "ProfessorID", nullable = false)
    private Integer professorId;

    @Id
    @Column(name = "RoomNumber", nullable = false)
    private String roomNumber;

    @Id
    @Column(name = "StartTime", nullable = false)
    private Timestamp startTime;

    @Column(name = "EndTime", nullable = false)
    private Timestamp endTime;

    // Getters and Setters
    public Integer getProfessorId() { return professorId; }
    public void setProfessorId(Integer professorId) { this.professorId = professorId; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public Timestamp getStartTime() { return startTime; }
    public void setStartTime(Timestamp startTime) { this.startTime = startTime; }

    public Timestamp getEndTime() { return endTime; }
    public void setEndTime(Timestamp endTime) { this.endTime = endTime; }

}

