package dev.zalaris.alumni.common.web.error;

import lombok.Data;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Data
@Getter
public class ErrorResponse {
  private int status;
  private OffsetDateTime timestamp;
  private String path;
  private List<Error> errors;

  public ErrorResponse(int status, String path, Error... errors) {
    this(status, path, List.of(errors));
  }

  public ErrorResponse(int status, String path, List<Error> errors) {
    this.status = status;
    this.path = path;
    this.errors = errors;
    this.timestamp = OffsetDateTime.now();
  }

  public Map<String, Object> toAttributeMap() {
    return Map.of(
      "status", status,
      "timestamp", timestamp,
      "path", path,
      "errors", errors
    );
  }

}
