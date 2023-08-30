package dev.zalaris.alumni.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zalaris.alumni.common.web.error.handler.ApiAccessDeniedHandler;
import dev.zalaris.alumni.common.web.error.handler.ApiAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.util.Set;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
  prePostEnabled = true
)
@RequiredArgsConstructor
class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Value("${alumni.app.security.enabled:true}")
  private Boolean isSecurityEnabled;

  @Value("${alumni.app.jwt.issuer-uri}")
  private Set<String> jwtIssuers;

  private final ObjectMapper objectMapper;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    if (isSecurityEnabled) {
      var authenticationManagerResolver = new JwtIssuerAuthenticationManagerResolver(jwtIssuers);
      http.cors()
          .and()
            .authorizeRequests().antMatchers("/actuator/health").permitAll()
          .and()
            .authorizeRequests().anyRequest().authenticated()
          .and()
            .oauth2ResourceServer(oauth -> oauth
              .authenticationManagerResolver(authenticationManagerResolver)
              .authenticationEntryPoint(new ApiAuthenticationEntryPoint(objectMapper))
              .accessDeniedHandler(new ApiAccessDeniedHandler(objectMapper)));
    } else {
      http.cors()
        .and()
        .csrf().disable()
        .authorizeRequests().anyRequest().permitAll();
    }
  }
}
