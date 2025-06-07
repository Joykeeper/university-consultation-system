package com.example.consultationsystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name="Room")
public class Room {

    @Version
    private Integer version;

    @Id
    @Column(name = "Number", nullable = false)
    private String number;

    // Getters and Setters
    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }
}