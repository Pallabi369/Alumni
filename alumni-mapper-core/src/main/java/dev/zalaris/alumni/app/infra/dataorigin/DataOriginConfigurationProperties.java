package dev.zalaris.alumni.app.infra.dataorigin;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@ConfigurationProperties(prefix = "alumni.data-origin")
@Validated
class DataOriginConfigurationProperties {

  /**
   * The strategy to select the data origin. The default value: policy.
   * Values: [policy, alumni]
   */
  @NotNull private String strategy = "policy";

  @NestedConfigurationProperty @Valid private PolicyProperties policy = new PolicyProperties();

  @Data
  public static class PolicyProperties {
    @NotNull private String sapTarget = "B2C_1_Business";
  }

}
