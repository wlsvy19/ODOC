package com.example.board.shop.question;

import com.example.board.shop.auth.UserPrincipal;
import com.example.board.shop.common.ApiException;
import com.example.board.shop.common.NotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<QuestionResponse> findAll(UserPrincipal user) {
        List<Question> questions = user.admin()
                ? questionRepository.findAllByOrderByIdDesc()
                : questionRepository.findAllByAuthorOrderByIdDesc(user.username());
        return questions.stream().map(QuestionResponse::from).toList();
    }

    @Transactional
    public QuestionResponse create(UserPrincipal user, QuestionRequest request) {
        Question question = new Question(user.username(), request.title(), request.content());
        return QuestionResponse.from(questionRepository.save(question));
    }

    @Transactional
    public QuestionResponse update(UserPrincipal user, Long id, QuestionRequest request) {
        Question question = findQuestion(id);
        requireOwnerOrAdmin(user, question);
        question.update(request.title(), request.content());
        return QuestionResponse.from(question);
    }

    @Transactional
    public QuestionResponse answer(UserPrincipal admin, Long id, AnswerRequest request) {
        Question question = findQuestion(id);
        question.answer(request.answer(), admin.username());
        return QuestionResponse.from(question);
    }

    @Transactional
    public void delete(UserPrincipal user, Long id) {
        Question question = findQuestion(id);
        requireOwnerOrAdmin(user, question);
        questionRepository.delete(question);
    }

    private Question findQuestion(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Question not found: " + id));
    }

    private void requireOwnerOrAdmin(UserPrincipal user, Question question) {
        if (!user.admin() && !question.getAuthor().equals(user.username())) {
            throw new ApiException("You can only manage your own questions.");
        }
    }
}

