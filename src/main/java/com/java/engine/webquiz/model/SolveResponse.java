package com.java.engine.webquiz.model;

import lombok.Getter;

@Getter
public class SolveResponse {

    private final boolean success;
    private final String feedback;


    public SolveResponse(boolean success, String feedback) {
        this.success = success;
        this.feedback = feedback;
    }
}
