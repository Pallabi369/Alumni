package dev.zalaris.alumni.app.token.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class TestSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
      http.cors()
        .and()
        .csrf().disable() // Disabled to prevent 403 during tests on missing csrf token
        .authorizeRequests().anyRequest().authenticated();
  }
}
