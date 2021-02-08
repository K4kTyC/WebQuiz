package com.java.engine.webquiz.repository;

import com.java.engine.webquiz.model.Quiz;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface QuizRepository extends PagingAndSortingRepository<Quiz, Long> {
}
