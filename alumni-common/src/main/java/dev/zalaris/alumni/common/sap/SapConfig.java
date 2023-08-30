package dev.zalaris.alumni.common.sap;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(SapApiConfigurationProperties.class)
@Import(SapWebClientConfiguration.class)
public class SapConfig {

  private final SapApiConfigurationProperties properties;
  private final WebClient webClient;

  @Bean
  FetchPayrollResults fetchPayrollResults() {
    return new FetchPayrollResultsService(webClient, properties);
  }

  @Bean
  FetchPersonalDataContainer fetchPersonalDataContainer() {
    return new FetchPersonalDataContainerService(webClient, properties);
  }

  @Bean
  FetchPayslipFile fetchPayslipFile() {
    return new FetchPayslipFileService(webClient, properties);
  }

}
