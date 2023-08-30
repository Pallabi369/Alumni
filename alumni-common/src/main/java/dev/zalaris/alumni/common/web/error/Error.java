package dev.zalaris.alumni.common.web.error;

public record Error(String code, String message) implements ErrorSchema {

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
