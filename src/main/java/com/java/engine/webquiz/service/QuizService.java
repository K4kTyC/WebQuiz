package com.java.engine.webquiz.service;

import com.java.engine.webquiz.model.*;
import com.java.engine.webquiz.model.AnswerRequest;
import com.java.engine.webquiz.model.SolveResponse;
import com.java.engine.webquiz.repository.CompletedQuizRepository;
import com.java.engine.webquiz.repository.OptionRepository;
import com.java.engine.webquiz.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final OptionRepository optionRepository;
    private final CompletedQuizRepository completedQuizRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository,
                       OptionRepository optionRepository,
                       CompletedQuizRepository completedQuizRepository) {
        this.quizRepository = quizRepository;
        this.optionRepository = optionRepository;
        this.completedQuizRepository = completedQuizRepository;
    }

    public Page<QuizDto> getAllQuizzes(int pageNumber) {
        PageRequest paging = PageRequest.of(pageNumber, 10);
        Page<Quiz> pagedQuizzes = quizRepository.findAll(paging);

        return pagedQuizzes.map(QuizDto::new);
    }

    public QuizDto createQuiz(QuizDto newQuiz, User quizAuthor) {
        Quiz quiz = new Quiz(newQuiz);
        quiz.setAuthor(quizAuthor);
        return new QuizDto(save(quiz));
    }

    public SolveResponse solveQuiz(long id, AnswerRequest answer, Quiz quiz, User currentUser) {
        List<Option> answerOptions = optionRepository.findByQuizIdAndAnswerTrue(id);
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
        completedQuiz.setQuiz(quiz);
        completedQuiz.setUser(currentUser);
        saveCompletedQuiz(completedQuiz);

        quiz.addCompletion(completedQuiz);
        save(quiz);

        return new SolveResponse(true, "Congratulations, you're right!");
    }

    public void saveCompletedQuiz(CompletedQuiz completedQuiz) {
        completedQuizRepository.save(completedQuiz);
    }

    public Page<CompletedQuizDto> getCompletedQuizzesByUserId(int pageNumber, long id) {
        PageRequest paging = PageRequest.of(pageNumber, 10);
        Page<CompletedQuiz> paged = completedQuizRepository.findByUserIdOrderByCompletedAtDesc(id, paging);

        return paged.map(CompletedQuizDto::new);
    }

    public Optional<Quiz> findById(long id) {
        return quizRepository.findById(id);
    }

    public void deleteById(long id) {
        quizRepository.deleteById(id);
    }

    public Quiz save(Quiz quiz) {
        return quizRepository.save(quiz);
    }
}
