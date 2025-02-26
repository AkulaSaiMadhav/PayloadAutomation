//package com.madhav.payloadgeneration.exceptions;
//
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.NoArgsConstructor;
//import org.apache.tomcat.websocket.AuthenticationException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.ErrorResponse;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//import java.io.IOException;
//
//@ControllerAdvice
//public class GlobalExceptionHandler extends RuntimeException {
//
//    @ExceptionHandler(AuthenticationException.class)
//    public ResponseEntity handleAuthorizationException(
//             HttpServletResponse response) throws IOException {
//        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//    }
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity illegalException(
//            HttpServletResponse response) {
//        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//    }
//
//
//}
