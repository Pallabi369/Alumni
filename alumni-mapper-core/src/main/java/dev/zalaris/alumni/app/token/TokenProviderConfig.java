package dev.zalaris.alumni.app.token;


import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.requests.GraphServiceClient;
import dev.zalaris.alumni.app.token.usecase.GetUserSsidFromSignicatInteractor;
import dev.zalaris.alumni.app.token.usecase.GetUserSsidUseCase;
import dev.zalaris.alumni.app.token.usecase.UpdateUserDataB2cInteractor;
import dev.zalaris.alumni.app.token.usecase.UpdateUserDataUseCase;
import okhttp3.Request;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@EnableConfigurationProperties(TokenProviderProperties.class)
@Configuration
public class TokenProviderConfig {

  @Bean
  GetUserSsidUseCase signicatInteractor(TokenProviderProperties properties) {
    RestTemplate restTemplate = new RestTemplate(
      Arrays.asList(
        new FormHttpMessageConverter(),
        new OAuth2AccessTokenResponseHttpMessageConverter(),
        new MappingJackson2HttpMessageConverter()));
    restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
    return new GetUserSsidFromSignicatInteractor(properties, restTemplate);
  }

  @Bean
  GraphServiceClient<Request> getGraphClient(TokenProviderProperties properties) {
    var b2c = properties.getB2c();

    final ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
      .clientId(b2c.getClientId())
      .clientSecret(b2c.getClientSecret())
      .tenantId(b2c.getTenantId())
      .build();

    final TokenCredentialAuthProvider tokenCredentialAuthProvider = new TokenCredentialAuthProvider(
      List.of("https://graph.microsoft.com/.default"), clientSecretCredential);

    GraphServiceClient<Request> graphClient = GraphServiceClient.builder()
      .authenticationProvider(tokenCredentialAuthProvider)
      .buildClient();

    return graphClient;
  }

  @Bean
  UpdateUserDataUseCase b2cUpdateInteractor(TokenProviderProperties properties) {
    return new UpdateUserDataB2cInteractor(properties, getGraphClient(properties));
  }

}
