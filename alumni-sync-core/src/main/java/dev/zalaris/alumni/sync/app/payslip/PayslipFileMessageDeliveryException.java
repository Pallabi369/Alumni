package dev.zalaris.alumni.sync.app.payslip;

import dev.zalaris.alumni.common.web.error.ErrorSchema;

import static dev.zalaris.alumni.common.web.error.ErrorCode.PAYSLIP_MSG_UNDELIVERED;

class PayslipFileMessageDeliveryException extends RuntimeException implements ErrorSchema {

  private final String code;

  public PayslipFileMessageDeliveryException(PayslipFileMessage payslipFileSyncRequest) {
    super(
      String.format("Delivering message to sync payslip [fileIdentifier:%s] for employee [zalarisId:%s] failed",
        payslipFileSyncRequest.fileIdentifier(),
        payslipFileSyncRequest.zalarisId()));
    this.code = PAYSLIP_MSG_UNDELIVERED;
  }

  public PayslipFileMessageDeliveryException(PayslipFileMessage payslipFileSyncRequest, Exception exception) {
    super(
      String.format("Delivering message to sync payslip [fileIdentifier:%s] for employee [zalarisId:%s] failed",
        payslipFileSyncRequest.fileIdentifier(),
        payslipFileSyncRequest.zalarisId()),
      exception);
    this.code = PAYSLIP_MSG_UNDELIVERED;
  }

  @Override
  public String getCode() {
    return code;
  }
}
