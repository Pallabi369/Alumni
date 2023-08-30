package dev.zalaris.alumni.app.employee.domain;

import dev.zalaris.alumni.common.web.error.ErrorSchema;

import static dev.zalaris.alumni.common.web.error.ErrorCode.EMPLOYMENT_HISTORY_NOT_FOUND;

public class EmploymentHistoryNotFoundException extends RuntimeException implements ErrorSchema {

  private final String code;

  private EmploymentHistoryNotFoundException(String ssid, String zalarisId) {
    super(String.format("No employment history found for %s", (ssid != null ? "ssid" + ssid : "zalarisId:" + zalarisId)));
    this.code = EMPLOYMENT_HISTORY_NOT_FOUND;
  }

  public String getCode() {
    return code;
  }

  public static EmploymentHistoryNotFoundException of(String ssid, String zalarisId) {
    return new EmploymentHistoryNotFoundException(ssid, zalarisId);
  }
}
