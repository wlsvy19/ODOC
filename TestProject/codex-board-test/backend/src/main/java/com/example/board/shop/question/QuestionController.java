package com.example.board.shop.question;

import com.example.board.shop.auth.AuthService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;
    private final AuthService authService;

    public QuestionController(QuestionService questionService, AuthService authService) {
        this.questionService = questionService;
        this.authService = authService;
    }

    @GetMapping
    public List<QuestionResponse> findAll(@RequestHeader("X-User-Id") String username) {
        return questionService.findAll(authService.requireUser(username));
    }

    @PostMapping
    public ResponseEntity<QuestionResponse> create(
            @RequestHeader("X-User-Id") String username,
            @Valid @RequestBody QuestionRequest request
    ) {
        QuestionResponse response = questionService.create(authService.requireUser(username), request);
        return ResponseEntity.created(URI.create("/api/questions/" + response.id())).body(response);
    }

    @PutMapping("/{id}")
    public QuestionResponse update(
            @RequestHeader("X-User-Id") String username,
            @PathVariable Long id,
            @Valid @RequestBody QuestionRequest request
    ) {
        return questionService.update(authService.requireUser(username), id, request);
    }

    @PutMapping("/{id}/answer")
    public QuestionResponse answer(
            @RequestHeader("X-User-Id") String username,
            @PathVariable Long id,
            @Valid @RequestBody AnswerRequest request
    ) {
        return questionService.answer(authService.requireAdmin(username), id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestHeader("X-User-Id") String username, @PathVariable Long id) {
        questionService.delete(authService.requireUser(username), id);
        return ResponseEntity.noContent().build();
    }
}

