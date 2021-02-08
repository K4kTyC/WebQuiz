package com.java.engine.webquiz.repository;

import com.java.engine.webquiz.model.CompletedQuiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CompletedQuizRepository extends PagingAndSortingRepository<CompletedQuiz, Long> {
    Page<CompletedQuiz> findByUserIdOrderByCompletedAtDesc(long id, Pageable pageable);
}
