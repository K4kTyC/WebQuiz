package com.java.engine.webquiz.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter
public class User {

    @Id
    @GeneratedValue(generator = "user-sequence-generator")
    @GenericGenerator(
            name = "user-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "user_sequence"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private long id;

    private String email;

    private String password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author")
    private List<Quiz> quizzes;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<CompletedQuiz> completedQuizzes;


    public User() { }

    public User(UserDto userDto) {
        this.email = userDto.getEmail();
        this.password = userDto.getPassword();
    }
}
