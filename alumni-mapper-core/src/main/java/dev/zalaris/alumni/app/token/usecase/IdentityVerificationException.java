package dev.zalaris.alumni.app.token.usecase;

public class IdentityVerificationException extends RuntimeException {

  public IdentityVerificationException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
