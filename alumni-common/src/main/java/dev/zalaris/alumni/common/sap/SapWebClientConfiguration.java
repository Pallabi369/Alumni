package dev.zalaris.alumni.common.sap;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(SapApiConfigurationProperties.class)
class SapWebClientConfiguration {

  private final SapApiConfigurationProperties properties;

  @ConditionalOnProperty(value = "sap.api.enabled", havingValue = "true")
  @Bean
  ReactiveClientRegistrationRepository getRegistration() {
    Oauth2ClientSettings oauth2 = properties.getOauth2();
    ClientRegistration registration = ClientRegistration
      .withRegistrationId(oauth2.getClientName())
      .tokenUri(oauth2.getTokenUri())
      .clientId(oauth2.getClientId())
      .clientSecret(oauth2.getClientSecret())
      .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
      .scope(oauth2.getScope())
      .build();
    return new InMemoryReactiveClientRegistrationRepository(registration);
  }


  @ConditionalOnProperty(value = "sap.api.enabled", havingValue = "true")
  @Bean
  WebClientCustomizer oauth2WebClientCustomizer(ReactiveClientRegistrationRepository clientRegistrations) {
    return webClientBuilder -> {
      Oauth2ClientSettings oauth2 = properties.getOauth2();

      InMemoryReactiveOAuth2AuthorizedClientService clientService = new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrations);
      AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager = new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrations, clientService);
      ServerOAuth2AuthorizedClientExchangeFilterFunction oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
      oauth.setDefaultClientRegistrationId(oauth2.getClientName());
      webClientBuilder.filter(oauth);
    };
  }

  @Bean
  WebClient sapWebClient(WebClient.Builder builder) {
    return builder.build();
  }

}
