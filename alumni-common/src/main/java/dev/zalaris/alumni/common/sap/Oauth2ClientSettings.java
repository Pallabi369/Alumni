package dev.zalaris.alumni.common.sap;

import lombok.Data;

@Data
public class Oauth2ClientSettings {
  private String clientName;
  private String tokenUri;
  private String clientId;
  private String clientSecret;
  private String scope;
}
