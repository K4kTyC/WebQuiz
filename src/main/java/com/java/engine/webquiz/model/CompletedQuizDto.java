package com.java.engine.webquiz.model;

import java.time.LocalDateTime;

public class CompletedQuizDto {

    private long id;
    private LocalDateTime completedAt;


    public CompletedQuizDto() { }

    public CompletedQuizDto(CompletedQuiz completedQuiz) {
        this.id = completedQuiz.getQuiz().getId();
        this.completedAt = completedQuiz.getCompletedAt();
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
}
