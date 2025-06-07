package com.example.consultationsystem.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

public class LessonId implements Serializable {
    private Integer professorId;
    private String roomNumber;
    private Timestamp startTime;

    public Integer getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Integer professorId) {
        this.professorId = professorId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    // Default constructor
    public LessonId() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LessonId lessonId = (LessonId) o;
        return Objects.equals(professorId, lessonId.professorId) && Objects.equals(roomNumber, lessonId.roomNumber) && Objects.equals(startTime, lessonId.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(professorId, roomNumber, startTime);
    }
}
