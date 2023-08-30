package dev.zalaris.alumni.app.infra.access;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Optional;

class AuthenticationClaimAccessor implements ClaimAccessor {

  @SuppressWarnings("unchecked")
  public <T> Optional<T> get(String claimName) {
    var authentication = obtainAuthentication();
    if (JwtAuthenticationToken.class.equals(authentication.getClass())) {
      var jwt = (Jwt) authentication.getPrincipal();
      return Optional.ofNullable((T) jwt.getClaims().get(claimName));
    }
    return Optional.empty();
  }

  private Authentication obtainAuthentication() {
    var context = SecurityContextHolder.getContext();
    if (context == null) {
      throw new IllegalArgumentException("No securityContext found");
    }

    var authentication = context.getAuthentication();
    if (authentication == null) {
      throw new IllegalArgumentException("No authentication information is available");
    }
    return authentication;
  }

}
