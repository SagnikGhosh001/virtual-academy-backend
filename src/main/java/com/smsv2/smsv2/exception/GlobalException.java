package com.smsv2.smsv2.exception;

import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalException {
	private final SimpMessagingTemplate messagingTemplate;

    public GlobalException(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    @ExceptionHandler(WebSocketException.class)
    @SendToUser("/queue/errors")
    public String handleWebSocketException(WebSocketException ex) {
        return ex.getMessage();
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> resourceNotFoundHandling(ResourceNotFoundException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceBadRequestException.class)
    public ResponseEntity<ErrorDetails> resourceBadRequestHandling(ResourceBadRequestException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceInternalServerErrorException.class)
    public ResponseEntity<ErrorDetails> handleResourceInternalServerError(ResourceInternalServerErrorException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> globalExceptionHandling(Exception exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
