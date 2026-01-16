package study.ronoyaro.producer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalHandleProducerAdviceController {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorMessageDefault> handleResponseStatusException(NotFoundException e) {

        var errorMessage = new ErrorMessageDefault(HttpStatus.NOT_FOUND.value(), e.getReason());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }
}
