package dev.zalaris.alumni.sync.config;

import dev.zalaris.alumni.common.web.error.handler.GlobalErrorAttributes;
import dev.zalaris.sync.Redelivery;
import dev.zalaris.sync.StreamMessageAzureSupport;
import dev.zalaris.sync.StreamMessageSupport;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
class SyncConfig {

  @Bean
  public ErrorAttributes errorAttributes() {
    return new GlobalErrorAttributes();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_PROTOTYPE)
  public StreamMessageSupport<?> createStreamMessageSupport(Redelivery redelivery) {
    return new StreamMessageAzureSupport<>(redelivery);
  }
}
