package dev.zalaris.alumni.app.config;

import dev.zalaris.alumni.app.employee.domain.EmployeeNotFoundException;
import dev.zalaris.alumni.app.employee.domain.EmploymentHistoryNotFoundException;
import dev.zalaris.alumni.app.employee.domain.PayslipNotFoundException;
import dev.zalaris.alumni.common.web.error.Error;
import dev.zalaris.alumni.common.web.error.ErrorResponse;
import dev.zalaris.alumni.common.web.error.ErrorSchema;
import dev.zalaris.alumni.common.web.error.handler.GlobalRestControllerAdvice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class MapperRestControllerAdvice extends GlobalRestControllerAdvice {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler({EmployeeNotFoundException.class,
    EmploymentHistoryNotFoundException.class, PayslipNotFoundException.class})
  public ErrorResponse handleNotFoundException(HttpServletRequest request, ErrorSchema exception) {
    log.info("EntityNotFound: {}", exception.getMessage(), (Exception)exception);
    return new ErrorResponse(HttpStatus.NOT_FOUND.value(), request.getRequestURI(),
      new Error(exception.getCode(), exception.getMessage()));
  }

}
