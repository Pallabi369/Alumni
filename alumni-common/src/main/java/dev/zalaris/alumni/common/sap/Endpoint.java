package dev.zalaris.alumni.common.sap;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.Duration;

@Data
@Validated
public class Endpoint {
  @NotNull private String url;
  private Duration timeout = Duration.ofSeconds(15);
}
