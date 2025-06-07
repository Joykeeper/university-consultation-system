package com.example.consultationsystem.exception;

public class NoStudentFoundException extends RuntimeException{

    public NoStudentFoundException() {
        super("No student with this id in database");
    }
}
