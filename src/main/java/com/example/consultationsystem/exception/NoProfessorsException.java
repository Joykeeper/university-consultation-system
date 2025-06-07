package com.example.consultationsystem.exception;

public class NoProfessorsException extends RuntimeException{

    public NoProfessorsException() {
        super("No professors in database");
    }
}
