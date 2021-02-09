package com.java.engine.webquiz.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "options")
@Getter @Setter
public class Option {

    @Id
    @GeneratedValue
    private long id;

    private String text;

    private boolean answer;

    private int num;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false, updatable = false)
    private Quiz quiz;
}
