package com.turism.services.repositories;

import com.turism.services.models.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, String> {
    Page<Question> findByContentContentId(String questionId, Pageable pageable);
}
