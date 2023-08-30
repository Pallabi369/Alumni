package dev.zalaris.alumni.common.sap;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@ConfigurationProperties(prefix = "sap.api")
@Data
@Validated
public class SapApiConfigurationProperties {

  private boolean enabled = false;

  @NestedConfigurationProperty private Oauth2ClientSettings oauth2;
  @NestedConfigurationProperty private Endpoints endpoint;

  private String subscriptionKey = "";

  @Data
  public static class Endpoints {
    @NestedConfigurationProperty @Valid private Endpoint personalData;
    @NestedConfigurationProperty @Valid private Endpoint payrollData;
    @NestedConfigurationProperty @Valid private Endpoint payslipData;
  }
}
