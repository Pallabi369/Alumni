package dev.zalaris.alumni.app.token.usecase;

import dev.zalaris.alumni.app.token.TokenProviderProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
public class GetUserSsidFromSignicatInteractor implements GetUserSsidUseCase {

  private final TokenProviderProperties properties;
  private final RestTemplate restTemplate;

  public String getSsid(String code) {
      var encryptedNationalId = getUserInfo(getAccessToken(code));
      return encryptedNationalId;
  }

  OAuth2AccessToken getAccessToken(String code) {
    var signicat = properties.getSignicat();
    MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
    parameters.add(OAuth2ParameterNames.GRANT_TYPE, AuthorizationGrantType.AUTHORIZATION_CODE.getValue());
    parameters.add(OAuth2ParameterNames.REDIRECT_URI, signicat.getRedirectUri());
    parameters.add(OAuth2ParameterNames.CODE, code);

    URI uri = UriComponentsBuilder.fromUriString(signicat.getTokenUri()).build().toUri();
    HttpHeaders headers = new HttpHeaders();
    headers.setBasicAuth(signicat.getClientId(), signicat.getClientSecret());
    var request = new RequestEntity<>(parameters, headers, HttpMethod.POST, uri);

    try {

      var response = this.restTemplate.exchange(request, OAuth2AccessTokenResponse.class);
      OAuth2AccessTokenResponse tokenResponse = response.getBody();
      log.info("The accessToken retrieved successfully");
      return tokenResponse.getAccessToken();

    } catch (RuntimeException e) {
      var msg = "Error retrieving access token from given code";
      log.error(String.format("%s [code:%s]", msg, code), e);
      throw new IdentityVerificationException(msg, e);
    }
  }

  String getUserInfo(OAuth2AccessToken accessToken) {
    URI uri = UriComponentsBuilder.fromUriString(properties.getSignicat().getUserInfoUri()).build().toUri();
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken.getTokenValue());
    var request = new RequestEntity<>(headers, HttpMethod.POST, uri);

    try {

      var response = this.restTemplate.exchange(request, HashMap.class);
      var claims = response.getBody();
      var nationalId = (String) Objects.requireNonNull(claims).get("signicat.national_id");
      var encryptedSsid = DigestUtils.sha256Hex(nationalId);
      log.info("UserInfo {} retrieved successfully", encryptedSsid);
      return encryptedSsid;

    } catch (RuntimeException e) {
      var msg = "Error obtaining ssid from signicat user info";
      log.error(msg, e);
      throw new IdentityVerificationException(msg, e);
    }
  }

}
