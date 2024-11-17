package com.turism.services.services;

import com.turism.services.models.Content;
import com.turism.services.models.Question;
import com.turism.services.models.User;
import com.turism.services.repositories.QuestionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<Question> getAllQuestionsByContent(Integer page, Integer size, String contentId) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return questionRepository.findByContentContentId(contentId, pageable).getContent();
    }

    public Question createQuestion(String question, String contentId, String userId) {
        return questionRepository.save(new Question(question, new Content(contentId), new User(userId)));
    }
}
