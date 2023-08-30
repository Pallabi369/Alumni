package dev.zalaris.alumni.common.web.error.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zalaris.alumni.common.web.error.Error;
import dev.zalaris.alumni.common.web.error.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static dev.zalaris.alumni.common.web.error.ErrorCode.UNAUTHORIZED;

@RequiredArgsConstructor
@Slf4j
public class ApiAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
                       AuthenticationException exception) throws IOException, ServletException {
    log.info("Unauthorized request to {}", request.getRequestURI(), exception);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    var content = objectMapper.writeValueAsString(
      new ErrorResponse(HttpServletResponse.SC_UNAUTHORIZED, request.getRequestURI(),
        new Error(UNAUTHORIZED, exception.getMessage())));
    response.getOutputStream().println(content);
  }
}
