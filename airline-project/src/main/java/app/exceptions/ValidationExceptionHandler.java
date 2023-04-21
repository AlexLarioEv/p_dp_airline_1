package app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@ControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<List<ResponseExceptionDTO>> handleException(BindException exception) {
        List<ResponseExceptionDTO> validationExceptionDto = bindFieldsExceptionsToList(exception, new ArrayList<>());
        return new ResponseEntity<>(validationExceptionDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ResponseExceptionDTO> handleException(SQLException exception) {
        ResponseExceptionDTO sqlExceptionDto = new ResponseExceptionDTO(exception.getMessage(), LocalDateTime.now());
        if (exception.getSQLState().equals("23505")) {
            return new ResponseEntity<>(sqlExceptionDto, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(sqlExceptionDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private List<ResponseExceptionDTO> bindFieldsExceptionsToList(
            BindException e,
            List<ResponseExceptionDTO> entityFieldsErrorList) {
        e.getFieldErrors().stream().forEach(a -> {
            entityFieldsErrorList.add(new ResponseExceptionDTO(a.getField() + " " + a.getDefaultMessage(), LocalDateTime.now()));
        });
        return entityFieldsErrorList;
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ResponseExceptionDTO> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getMessage());
        }
        ResponseExceptionDTO constraintViolationExceptionDto = new ResponseExceptionDTO(errors.toString(), LocalDateTime.now());
        return new ResponseEntity<>(constraintViolationExceptionDto, HttpStatus.BAD_REQUEST);
    }


}

