package com.java.engine.webquiz.repository;

import com.java.engine.webquiz.model.Option;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OptionRepository extends CrudRepository<Option, Long> {
    List<Option> findByQuiz_IdAndAnswerTrue(long id);
}
