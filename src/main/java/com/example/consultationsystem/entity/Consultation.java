package com.example.consultationsystem.entity;


import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "Consultation")
public class Consultation {

    @Version
    private Integer version = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "SNumber", nullable = false)
    private Integer sNumber;

    @Column(name = "RoomNumber", nullable = false)
    private String roomNumber;

    @Column(name = "ProfessorID", nullable = false)
    private Integer professorId;

    @Column(name = "startTime", nullable = false)
    private Timestamp startTime;

    public Integer getsNumber() {
        return sNumber;
    }

    public void setsNumber(Integer sNumber) {
        this.sNumber = sNumber;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Integer getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Integer professorId) {
        this.professorId = professorId;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

}
