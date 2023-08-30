package dev.zalaris.alumni.common.web.error;

public interface ErrorCode {

  String REST = "rest.api";

  String SERVER_ERROR = "server.error";
  String UNAUTHORIZED = "security.unauthorized";
  String FORBIDDEN = "security.forbidden";
  String EMPLOYEE_NOT_FOUND = "employee.notFound";
  String EMPLOYMENT_HISTORY_NOT_FOUND = "employmentHistory.notFound";
  String PAYSLIP_NOT_FOUND = "payslip.notFound";

  String IDENTITY_VERIFICATION_ERROR = "identityVerification.error";

  String VALIDATION = "validation.error";

  String EMPLOYEE_MSG_UNDELIVERED = "employeeMessage.undelivered";
  String PAYSLIP_MSG_UNDELIVERED = "payslipMessage.undelivered";

}
