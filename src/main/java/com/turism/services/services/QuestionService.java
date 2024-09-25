package com.turism.services.services;

import com.turism.services.models.Content;
import com.turism.services.models.Question;
import com.turism.services.models.User;
import com.turism.services.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> getAllQuestions(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return questionRepository.findAll();
    }

    public void CreateQuestion(String question, String contentId, String userId) {
        questionRepository.save(new Question(question, new Content(contentId), new User(userId)));
    }
}
