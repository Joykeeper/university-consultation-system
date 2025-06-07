package com.example.consultationsystem.dto;

import java.sql.Timestamp;

public class RescheduleRequest {

    private Timestamp newStartTime;

    public Timestamp getNewStartTime() {
        return newStartTime;
    }

    public void setNewStartTime(Timestamp newStartTime) {
        this.newStartTime = newStartTime;
    }


}
