package dev.zalaris.alumni.app.config;

import dev.zalaris.alumni.app.infra.logging.MDCInterceptor;
import dev.zalaris.alumni.common.web.error.handler.GlobalErrorAttributes;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
class MapperConfig implements WebMvcConfigurer {

  private final MDCInterceptor mdcInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(mdcInterceptor);
  }

  @Bean
  public ErrorAttributes errorAttributes() {
    return new GlobalErrorAttributes();
  }

}
