package dev.zalaris.alumni.app.employee.domain;

import dev.zalaris.alumni.common.web.error.ErrorSchema;

import static dev.zalaris.alumni.common.web.error.ErrorCode.EMPLOYEE_NOT_FOUND;

public class EmployeeNotFoundException extends RuntimeException implements ErrorSchema {

  private String code;

  private EmployeeNotFoundException(String zalarisId) {
    super(String.format("Employee %s not found", zalarisId));
    this.code = EMPLOYEE_NOT_FOUND;
  }

  public String getCode() {
    return code;
  }

  public static EmployeeNotFoundException of(String zalarisId) {
    return new EmployeeNotFoundException(zalarisId);
  }

}
