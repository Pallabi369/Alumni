package dev.zalaris.alumni.common.web.error.handler;

import dev.zalaris.alumni.common.web.error.Error;
import dev.zalaris.alumni.common.web.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Optional;

import static dev.zalaris.alumni.common.web.error.ErrorCode.*;

@Slf4j
public abstract class GlobalRestControllerAdvice {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ConstraintViolationException.class)
  public ErrorResponse handleConstraintViolationException(HttpServletRequest request,
                                                          ConstraintViolationException exception) {
    log.warn("ConstraintViolationException exception", exception);
    var errors = new ArrayList<Error>();
    for(var violation : exception.getConstraintViolations()) {
      String fieldName = "";
      var nodes = violation.getPropertyPath().iterator();
      while(nodes.hasNext()) {
        fieldName = nodes.next().toString();
      }
      errors.add(new Error(VALIDATION, fieldName + ": " + violation.getMessage()));
    }
    return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), request.getRequestURI(), errors);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ErrorResponse handle(HttpServletRequest request, MethodArgumentNotValidException exception) {
    log.warn("MethodArgumentNotValidException exception", exception);
    var errors = new ArrayList<Error>();
    for(var violation : exception.getFieldErrors()) {
      errors.add(new Error(VALIDATION, violation.getField() + ": " + violation.getDefaultMessage()));
    }
    return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), request.getRequestURI(), errors);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ErrorResponse handle(HttpServletRequest request, HttpMessageNotReadableException exception) {
    log.warn("HttpMessageNotReadableException exception", exception);
    var msg = exception.getRootCause() != null ?
      exception.getRootCause().getMessage() : exception.getMessage();
    return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), request.getRequestURI(),
      new Error(VALIDATION, msg));
  }

  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ExceptionHandler(AccessDeniedException.class)
  ErrorResponse handle(HttpServletRequest request, AccessDeniedException exception) {
    log.error("AccessDeniedException exception", exception);
    return new ErrorResponse(HttpStatus.FORBIDDEN.value(), request.getRequestURI(),
      new Error(FORBIDDEN, exception.getMessage()));
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public ErrorResponse handleGeneralException(HttpServletRequest request, Exception exception) {
    log.error("General server error", exception);
    return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), request.getRequestURI(),
      new Error(SERVER_ERROR, Optional.ofNullable(exception.getMessage()).orElse("")));
  }

}
