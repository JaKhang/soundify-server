package com.soundify.server.account.http;


import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.soundify.server.shared.exceptions.ErrorResponse;
import com.soundify.server.account.infrastructure.security.IllegalJwtException;
import com.soundify.server.account.domain.exceptions.IllegalTokenException;
import com.soundify.server.account.domain.exceptions.EmailAlreadyVerifiedException;

@ControllerAdvice
public class AccountExceptionHandler {

    @ExceptionHandler(IllegalJwtException.class)
    public ResponseEntity<ErrorResponse> handleIllegalJwtException(IllegalJwtException ex) {
        ErrorResponse response = new ErrorResponse(1001, ex.getMessage(), "Unauthorized", null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(IllegalTokenException.class)
    public ResponseEntity<ErrorResponse> handleIllegalTokenException(IllegalTokenException ex) {
        ErrorResponse response = new ErrorResponse(1002, ex.getMessage(), "Bad Request", null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(EmailAlreadyVerifiedException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyVerifiedException(EmailAlreadyVerifiedException ex) {
        ErrorResponse response = new ErrorResponse(1003, ex.getMessage(), "Conflict", null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        ErrorResponse response = new ErrorResponse(1004, ex.getMessage(), "Bad Credentials", null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientAuthenticationException(InsufficientAuthenticationException ex) {
        ErrorResponse response = new ErrorResponse(1005, ex.getMessage(), "Insufficient Authentication", null);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
    
    


    @ExceptionHandler(OAuth2AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleOAuth2AuthenticationException(OAuth2AuthenticationException ex) {
        ErrorResponse response = new ErrorResponse(1006, ex.getMessage(), "OAuth2 Authentication Exception", null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }


    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(1007, ex.getMessage(), "Username Not Found", null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        ErrorResponse response = new ErrorResponse(1008, ex.getMessage(), "Authorization Denied", null);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> handleDisDeniedException(DisabledException ex) {
        ErrorResponse response = new ErrorResponse(1008, ex.getMessage(), "Disabled Account", null);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

}
