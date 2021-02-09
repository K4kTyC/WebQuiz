package com.java.engine.webquiz.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quizzes")
@Getter @Setter
public class Quiz {

    @Id
    @GeneratedValue(generator = "quiz-sequence-generator")
    @GenericGenerator(
            name = "quiz-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "quiz_sequence"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private long id;

    private String title;

    private String text;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quiz")
    private List<Option> options = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User author;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quiz")
    private List<CompletedQuiz> completions = new ArrayList<>();


    public Quiz() { }

    public Quiz(QuizDto quiz) {
        this.title = quiz.getTitle();
        this.text = quiz.getText();

        for (int i = 0; i < quiz.getOptions().size(); i++) {
            Option option = new Option();
            option.setText(quiz.getOptions().get(i));
            option.setAnswer(quiz.getAnswer().contains(i));
            option.setNum(i);
            option.setQuiz(this);
            this.options.add(option);
        }
    }

    public void addCompletion(CompletedQuiz completedQuiz) {
        completions.add(completedQuiz);
    }
}
