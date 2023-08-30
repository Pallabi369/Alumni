package dev.zalaris.alumni.sync.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zalaris.alumni.common.web.error.handler.ApiAccessDeniedHandler;
import dev.zalaris.alumni.common.web.error.handler.ApiAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfig {

  @Value("${alumni.sync.security.enabled:true}")
  private Boolean isSecurityEnabled;

  private final ObjectMapper objectMapper;

  @Bean
  public SecurityFilterChain configure(HttpSecurity http) throws Exception {
    if (isSecurityEnabled) {
      http.cors()
        .and()
          .authorizeRequests().antMatchers("/actuator/health").permitAll()
        .and()
          .authorizeRequests().anyRequest().hasRole("AlumniSync")
        .and()
          .oauth2ResourceServer(oauth -> oauth
            .authenticationEntryPoint(new ApiAuthenticationEntryPoint(objectMapper))
            .accessDeniedHandler(new ApiAccessDeniedHandler(objectMapper))
            .jwt()
              .jwtAuthenticationConverter(jwtAuthenticationConverter()));

    } else {
      http.cors()
        .and()
        .csrf().disable()
        .authorizeRequests().anyRequest().permitAll();
    }
    return http.build();
  }

  private JwtAuthenticationConverter jwtAuthenticationConverter() {
    // create a custom JWT converter to map the "roles" from the token as granted authorities
    JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
    jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
    return jwtAuthenticationConverter;
  }

}
