package com.bugsolver.config;

import com.bugsolver.exception.BugSolverException;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${app.language-default}")
    private String appLanguageDefault;

    @Autowired
    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request
    ){
        var language = getLanguage((ServletWebRequest) request);

        var error = ErrorResponse.builder()
                .message("BAD REQUEST 400")
                .errors(getErrors(ex, language))
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error);
    }

    @ExceptionHandler({BugSolverException.class })
    public ResponseEntity<ErrorResponse> bugSolverExceptionHandler(
        BugSolverException exception,
        ServletWebRequest request
    ) {
        var language = getLanguage(request);

        var error = ErrorResponse.builder()
                .message(getMessage(exception.getMessageKey(), language))
                .path(request.getRequest().getRequestURI())
                .status(exception.getStatus().value())
                .timestamp(LocalDateTime.now())
                .errors(new ArrayList<>())
                .build();
        return ResponseEntity
            .status(exception.getStatus())
            .body(error);
    }


     private Locale getLanguage(ServletWebRequest request) {
        var language = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        return nonNull(language) && language.length() > 0 ? new Locale(language) : new Locale(appLanguageDefault);
    }

    private List<String> getErrors(MethodArgumentNotValidException exception, Locale language) {
        return exception.getFieldErrors().stream()
            .map(error -> getMessage(error.getDefaultMessage(), language))
            .collect(toList());
    }

    private String getMessage(String messageKey, Locale language) {
        return messageSource.getMessage(messageKey, null, language);
    }

    @Data
    @Builder
    static class ErrorResponse {
        private String path;
        private String message;
        private Integer status;
        private LocalDateTime timestamp;
        private List<String> errors;
    }
}
