package com.java.engine.webquiz.controller;

import com.java.engine.webquiz.payload.AnswerRequest;
import com.java.engine.webquiz.model.*;
import com.java.engine.webquiz.payload.SolveResponse;
import com.java.engine.webquiz.repository.OptionRepository;
import com.java.engine.webquiz.service.QuizService;
import com.java.engine.webquiz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;
    private final UserService userService;

    private final OptionRepository optionRepository;

    @Autowired
    public QuizController(QuizService quizService,
                          UserService userService,
                          OptionRepository optionRepository) {
        this.quizService = quizService;
        this.userService = userService;
        this.optionRepository = optionRepository;
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
        Quiz quiz = new Quiz(newQuiz);
        Optional<User> quizAuthor = userService.findByEmail(principal.getName());
        if (quizAuthor.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You need to log in or register to create quizzes");
        }
        quiz.setAuthor(quizAuthor.get());

        Quiz savedQuiz = quizService.save(quiz);
        return new QuizDto(savedQuiz);
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

        List<Option> answerOptions = optionRepository.findByQuiz_IdAndAnswerTrue(id);
        if (answerOptions.size() != answer.getAnswer().size()) {
            return new SolveResponse(false, "Wrong answer! Please, try again.");
        } else if (answer.getAnswer().size() != 0) {
            for (Option answerOption : answerOptions) {
                if (!answer.getAnswer().contains(answerOption.getNum())) {
                    return new SolveResponse(false, "Wrong answer! Please, try again.");
                }
            }
        }

        CompletedQuiz completedQuiz = new CompletedQuiz();
        completedQuiz.setCompletedAt(LocalDateTime.now());
        completedQuiz.setQuiz(quiz.get());
        completedQuiz.setUser(currentUser.get());
        userService.saveCompletedQuiz(completedQuiz);

        quiz.get().addCompletion(completedQuiz);
        quizService.save(quiz.get());

        return new SolveResponse(true, "Congratulations, you're right!");
    }

    @GetMapping("/completed")
    public Page<CompletedQuizDto> getAllCompletedQuizzesByUser(@RequestParam(defaultValue = "0") int page, Principal principal) {
        Optional<User> currentUser = userService.findByEmail(principal.getName());
        if (currentUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You need to log in or register to create quizzes");
        }

        return userService.getCompletedQuizzesByUserId(page, currentUser.get().getId());
    }
}
