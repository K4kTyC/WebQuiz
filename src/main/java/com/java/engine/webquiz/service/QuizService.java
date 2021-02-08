package com.java.engine.webquiz.service;

import com.java.engine.webquiz.model.Quiz;
import com.java.engine.webquiz.model.QuizDto;
import com.java.engine.webquiz.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuizService {

    private final QuizRepository quizRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }


    public Page<QuizDto> getAllQuizzes(int pageNumber) {
        PageRequest paging = PageRequest.of(pageNumber, 10);
        Page<Quiz> pagedQuizzes = quizRepository.findAll(paging);

        return pagedQuizzes.map(QuizDto::new);
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
