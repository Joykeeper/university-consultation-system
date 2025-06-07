package com.example.consultationsystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name="Student")
public class Student {

    @Version
    private Integer version;

    @Id
    @Column(name = "SNumber", nullable = false)
    private Integer sNumber;

    @Column(name = "FirstName", nullable = false)
    private String firstName;

    @Column(name = "LastName", nullable = false)
    private String lastName;

    // Getters and Setters
    public Integer getSNumber() { return sNumber; }
    public void setSNumber(Integer sNumber) { this.sNumber = sNumber; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
}
