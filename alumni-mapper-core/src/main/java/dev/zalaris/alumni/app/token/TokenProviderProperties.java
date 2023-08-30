package dev.zalaris.alumni.app.token;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "identity")
@Data
public class TokenProviderProperties {

  @NestedConfigurationProperty
  private Signicat signicat = new Signicat();

  @NestedConfigurationProperty
  @NotNull
  private B2C b2c = new B2C();

  @Data
  public static class Signicat {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String tokenUri = "https://preprod.signicat.com/oidc/token";
    private String userInfoUri = "https://preprod.signicat.com/oidc/userinfo";
  }

  @Data
  public static class B2C {
    private String clientId;
    private String clientSecret;
    private String tenantId;
    private String ssidProperty;
  }

}
