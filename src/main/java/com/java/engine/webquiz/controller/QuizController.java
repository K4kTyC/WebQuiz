package com.java.engine.webquiz.controller;

import com.java.engine.webquiz.model.CompletedQuizDto;
import com.java.engine.webquiz.model.Quiz;
import com.java.engine.webquiz.model.QuizDto;
import com.java.engine.webquiz.model.User;
import com.java.engine.webquiz.payload.AnswerRequest;
import com.java.engine.webquiz.payload.SolveResponse;
import com.java.engine.webquiz.service.QuizService;
import com.java.engine.webquiz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private QuizService quizService;
    private UserService userService;

    @Autowired
    public void setQuizService(QuizService quizService) {
        this.quizService = quizService;
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/{id}")
    public QuizDto getQuiz(@PathVariable long id) {
        Optional<Quiz> quiz = quizService.findById(id);
        if (quiz.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No quiz with id: " + id);
        }
        return new QuizDto(quiz.get());
    }

    @DeleteMapping("/{id}")
    public void deleteQuiz(@PathVariable long id, Principal principal) {
        Optional<Quiz> quiz = quizService.findById(id);
        if (quiz.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No quiz with id: " + id);
        }

        if (quiz.get().getAuthor().getEmail().equals(principal.getName())) {
            quizService.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have rights to modify this content");
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public Page<QuizDto> getAllQuizzes(@RequestParam(defaultValue = "0") int page) {
        return quizService.getAllQuizzes(page);
    }

    @PostMapping(consumes = "application/json")
    public QuizDto createQuiz(@RequestBody @Valid QuizDto newQuiz, Principal principal) {
        Optional<User> quizAuthor = userService.findByEmail(principal.getName());
        if (quizAuthor.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You need to log in or register to create quizzes");
        }

        return quizService.createQuiz(newQuiz, quizAuthor.get());
    }

    @PostMapping(value = "/{id}/solve", consumes = "application/json")
    public SolveResponse solveQuiz(@PathVariable long id, @RequestBody AnswerRequest answer, Principal principal) {
        Optional<Quiz> quiz = quizService.findById(id);
        if (quiz.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No quiz with id: " + id);
        }
        Optional<User> currentUser = userService.findByEmail(principal.getName());
        if (currentUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You need to log in or register to create quizzes");
        }

        return quizService.solveQuiz(id, answer, quiz.get(), currentUser.get());
    }

    @GetMapping("/completed")
    public Page<CompletedQuizDto> getAllCompletedQuizzesByUser(@RequestParam(defaultValue = "0") int page, Principal principal) {
        Optional<User> currentUser = userService.findByEmail(principal.getName());
        if (currentUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You need to log in or register to create quizzes");
        }

        return quizService.getCompletedQuizzesByUserId(page, currentUser.get().getId());
    }
}
