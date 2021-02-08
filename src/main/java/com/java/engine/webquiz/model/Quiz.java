package com.java.engine.webquiz.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quizzes")
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<CompletedQuiz> getCompletions() {
        return completions;
    }

    public void setCompletions(List<CompletedQuiz> completions) {
        this.completions = completions;
    }
}
