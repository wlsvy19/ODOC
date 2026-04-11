package com.fif.fifgg.dto.response.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

// 공통 에러 응답
@Getter
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
}