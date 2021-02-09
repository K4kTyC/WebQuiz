package com.java.engine.webquiz.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CompletedQuizDto {

    private long id;
    private LocalDateTime completedAt;


    public CompletedQuizDto() { }

    public CompletedQuizDto(CompletedQuiz completedQuiz) {
        this.id = completedQuiz.getQuiz().getId();
        this.completedAt = completedQuiz.getCompletedAt();
    }
}
