package dev.zalaris.alumni.app.employee.domain;

import dev.zalaris.alumni.common.web.error.ErrorSchema;

import static dev.zalaris.alumni.common.web.error.ErrorCode.PAYSLIP_NOT_FOUND;

public class PayslipNotFoundException  extends RuntimeException implements ErrorSchema {

  private String code;

  private PayslipNotFoundException(String name, String sequenceId) {
    super(String.format("Payslip %s - %s not found", name, sequenceId));
    this.code = PAYSLIP_NOT_FOUND;
  }

  public String getCode() {
    return code;
  }

  public static PayslipNotFoundException of(String name, String sequenceId) {
    return new PayslipNotFoundException(name, sequenceId);
  }

}