package com.soundify.server.account.http;


import com.soundify.server.account.domain.exceptions.TokenInvalidException;
import com.soundify.server.account.infrastructure.security.JwtInvalidException;
import com.soundify.server.shared.exceptions.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AccountExceptionHandler {

    @ExceptionHandler(JwtInvalidException.class)
    public ResponseEntity<ErrorResponse> handleJwtInvalidException(JwtInvalidException e) {
        return errorResponse(100, HttpStatus.FORBIDDEN, e.getMessage(), null);
    }

    @ExceptionHandler(TokenInvalidException.class)
    public ResponseEntity<ErrorResponse> handleJwtInvalidException(TokenInvalidException e) {
       return errorResponse(101, HttpStatus.FORBIDDEN, e.getMessage(), null);
    }




    private ResponseEntity<ErrorResponse> errorResponse(int errorCode, HttpStatus status ,String message, Object data) {
        ErrorResponse response = new ErrorResponse(errorCode, status.value(), message, data);
        return new ResponseEntity<>(response, status);
    }
}
