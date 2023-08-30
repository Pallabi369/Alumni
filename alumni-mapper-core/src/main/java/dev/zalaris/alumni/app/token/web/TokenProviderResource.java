package dev.zalaris.alumni.app.token.web;

import dev.zalaris.alumni.app.token.usecase.GetUserSsidUseCase;
import dev.zalaris.alumni.app.token.usecase.IdentityVerificationException;
import dev.zalaris.alumni.app.token.usecase.UpdateUserDataUseCase;
import dev.zalaris.alumni.common.web.error.Error;
import dev.zalaris.alumni.common.web.error.ErrorCode;
import dev.zalaris.alumni.common.web.error.ErrorResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/api/tokens")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "TokenProvider")
class TokenProviderResource {

  private final GetUserSsidUseCase getUserSsidUseCase;
  private final UpdateUserDataUseCase updateUserDataUseCase;

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  void token(@RequestParam String code, Authentication authentication) {
    var encryptedNationalId = getUserSsidUseCase.getSsid(code);
    updateUserDataUseCase.updateUserNationalId(authentication.getName(), encryptedNationalId);
  }

  @ExceptionHandler(OAuth2AuthorizationException.class)
  ResponseEntity<OAuth2Error> oauth2AuthorizationException(OAuth2AuthorizationException exception) {
    log.warn("The token could not be retrieved", exception);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getError());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(IdentityVerificationException.class)
  public ErrorResponse handle(HttpServletRequest request, IdentityVerificationException exception) {
    var msg = exception.getMessage();
    return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), request.getRequestURI(),
      new Error(ErrorCode.IDENTITY_VERIFICATION_ERROR, msg));
  }


}
