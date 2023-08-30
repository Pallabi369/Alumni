package dev.zalaris.alumni.app.infra.dataorigin;

import dev.zalaris.alumni.app.infra.access.ClaimAccessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DataOriginConfigurationProperties.class)
class DataOriginConfig {

  @Bean
  @ConditionalOnProperty(name = "alumni.data-origin.strategy", havingValue = "policy")
  DataOriginSelector dataSourceSelector(DataOriginConfigurationProperties properties, ClaimAccessor claimAccessor) {
    return new PolicyBasedDataOriginSelector(claimAccessor, properties);
  }

  @Bean
  @ConditionalOnProperty(name = "alumni.data-origin.strategy", havingValue = "alumni")
  DataOriginSelector forceAlumni() {
    return new AlumniDataOriginSelector();
  }

}
