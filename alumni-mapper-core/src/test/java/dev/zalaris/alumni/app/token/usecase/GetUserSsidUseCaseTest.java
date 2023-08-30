package dev.zalaris.alumni.app.token.usecase;

import dev.zalaris.alumni.app.token.TokenProviderProperties;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;


public class GetUserSsidUseCaseTest {

  TokenProviderProperties properties;
  RestTemplate restTemplate;
  MockRestServiceServer mockServer;
  GetUserSsidFromSignicatInteractor useCase;

  @BeforeEach
  public void init() {
    properties = tokenProviderProperties();
    restTemplate = new RestTemplate(
      Arrays.asList(
        new FormHttpMessageConverter(),
        new OAuth2AccessTokenResponseHttpMessageConverter(),
        new MappingJackson2HttpMessageConverter()));
    mockServer = MockRestServiceServer.createServer(restTemplate);
    useCase = new GetUserSsidFromSignicatInteractor(properties, restTemplate);
  }

  private TokenProviderProperties tokenProviderProperties() {
    var b2c = new TokenProviderProperties.B2C();
    b2c.setClientId("B2C_CLIENT_ID");
    var signicat = new TokenProviderProperties.Signicat();
    signicat.setClientId("SIGNICAT_CLIENT_ID");
    signicat.setClientSecret("SIGNICAT_CLIENT_SECRET");
    signicat.setTokenUri("/signicat/token");
    signicat.setRedirectUri("/signicat/redirect");
    signicat.setUserInfoUri("/signicat/userinfo");

    var props = new TokenProviderProperties();
    props.setB2c(b2c);
    props.setSignicat(signicat);
    return props;
  }


  @Test
  public void testGetAccessToken() throws Exception {
    var code = "123";
    var oauth2Token = "{\"access_token\":\"my-access-token\", \"token_type\":\"Bearer\", \"expires_in\":\"3600\"}";

    mockServer.expect(method(HttpMethod.POST))
      .andExpect(requestTo(properties.getSignicat().getTokenUri()))
      .andExpect(MockRestRequestMatchers.content().formDataContains(Map.of(
        OAuth2ParameterNames.GRANT_TYPE, AuthorizationGrantType.AUTHORIZATION_CODE.getValue(),
        OAuth2ParameterNames.REDIRECT_URI, properties.getSignicat().getRedirectUri(),
        OAuth2ParameterNames.CODE, code
      )))
      .andExpect(header(HttpHeaders.AUTHORIZATION, "Basic " +
        HttpHeaders.encodeBasicAuth(
          properties.getSignicat().getClientId(),
          properties.getSignicat().getClientSecret(),
          Charset.defaultCharset())))
      .andRespond(MockRestResponseCreators.withSuccess(oauth2Token, MediaType.APPLICATION_JSON));

    var token = useCase.getAccessToken(code);

    Assertions.assertNotNull(token, "token not null");
    Assertions.assertNotNull(token.getTokenValue(), "token value not null");
    Assertions.assertTrue("my-access-token".equalsIgnoreCase(token.getTokenValue()), "token not the same");
  }

  @Test
  public void testGetAccessTokenWith500() throws Exception {
    var code = "123";
    var oauth2Token = "{\"access_token\":\"my-access-token\", \"token_type\":\"Bearer\", \"expires_in\":\"3600\"}";

    mockServer.expect(method(HttpMethod.POST))
      .andExpect(requestTo(properties.getSignicat().getTokenUri()))
      .andExpect(header(HttpHeaders.AUTHORIZATION, "Basic " +
        HttpHeaders.encodeBasicAuth(
          properties.getSignicat().getClientId(),
          properties.getSignicat().getClientSecret(),
          Charset.defaultCharset())))
      .andRespond(MockRestResponseCreators.withServerError());

    var exception = Assertions.assertThrows(IdentityVerificationException.class, () -> {
      useCase.getAccessToken(code);
    });
  }


  @Test
  public void testGetUserInfo() throws Exception {
    var accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "my-access-token", Instant.now(), Instant.MAX);
    var userInfo = """
      {
      	"family_name": "Signicat",
      	"given_name": "John",
      	"locale": "SE",
      	"name": "John Signicat",
      	"signicat.national_id": "199010275312",
      	"sub": "KGMyh5FBCMTkEN934sOLyyBS0rPd4-up",
      	"subject.nameid.namequalifier": "BANKID-SE"
      }
      """;


    mockServer.expect(method(HttpMethod.POST))
      .andExpect(requestTo(properties.getSignicat().getUserInfoUri()))
      .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken.getTokenValue()))
      .andRespond(MockRestResponseCreators.withSuccess(userInfo, MediaType.APPLICATION_JSON));

    var ssid = useCase.getUserInfo(accessToken);

    Assertions.assertNotNull(ssid, "ssid not null");
    Assertions.assertEquals(DigestUtils.sha256Hex("199010275312"), ssid);
  }

  @Test
  public void testGetUserInfoNoSsidInClaims() throws Exception {
    var accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "my-access-token", Instant.now(), Instant.MAX);
    var userInfoWithoutSSid = """
      {
      	"family_name": "Signicat",
      	"given_name": "John",
      	"locale": "SE",
      	"name": "John Signicat",
      	"sub": "KGMyh5FBCMTkEN934sOLyyBS0rPd4-up",
      	"subject.nameid.namequalifier": "BANKID-SE"
      }
      """;


    mockServer.expect(method(HttpMethod.POST))
      .andExpect(requestTo(properties.getSignicat().getUserInfoUri()))
      .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken.getTokenValue()))
      .andRespond(MockRestResponseCreators.withSuccess(userInfoWithoutSSid, MediaType.APPLICATION_JSON));

    var exception = Assertions.assertThrows(IdentityVerificationException.class, () -> {
      useCase.getUserInfo(accessToken);
    });
  }

}
