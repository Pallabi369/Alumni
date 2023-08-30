package dev.zalaris.alumni.app.infra.access;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccessConfig {

  @Bean
  ClaimAccessor claimAccessor() {
    return new AuthenticationClaimAccessor();
  }

  @Bean
  AccessControl accessControl(ClaimAccessor claimAccessor) {
    return new ClaimAccessControl(claimAccessor);
  }

}
