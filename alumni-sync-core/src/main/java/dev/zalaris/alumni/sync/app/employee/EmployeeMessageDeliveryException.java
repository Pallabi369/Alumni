package dev.zalaris.alumni.sync.app.employee;

import dev.zalaris.alumni.common.web.error.ErrorSchema;

import static dev.zalaris.alumni.common.web.error.ErrorCode.EMPLOYEE_MSG_UNDELIVERED;
import static java.lang.String.format;

public class EmployeeMessageDeliveryException extends RuntimeException implements ErrorSchema {

  private String code;

  public EmployeeMessageDeliveryException(String zalarisId) {
    super(format("Delivering message to sync employee [zalarisId:%s] failed", zalarisId));
    this.code = EMPLOYEE_MSG_UNDELIVERED;
  }

  public EmployeeMessageDeliveryException(String zalarisId, Exception exception) {
    super(format("Delivering message to sync employee [zalarisId:%s] failed", zalarisId), exception);
    this.code = EMPLOYEE_MSG_UNDELIVERED;
  }

  @Override
  public String getCode() {
    return code;
  }
}
