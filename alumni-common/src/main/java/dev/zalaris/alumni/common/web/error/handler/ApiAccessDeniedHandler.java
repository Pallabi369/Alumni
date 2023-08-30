package dev.zalaris.alumni.common.web.error.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zalaris.alumni.common.web.error.Error;
import dev.zalaris.alumni.common.web.error.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static dev.zalaris.alumni.common.web.error.ErrorCode.FORBIDDEN;

@RequiredArgsConstructor
@Slf4j
public class ApiAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
                     AccessDeniedException exception) throws IOException, ServletException {
    log.info("Access denied exception when requesting {}", request.getRequestURI(), exception);

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);

    var content = objectMapper.writeValueAsString(
      new ErrorResponse(HttpServletResponse.SC_FORBIDDEN,
        request.getRequestURI(), new Error(FORBIDDEN, exception.getMessage())));
    response.getWriter().println(content);
  }
}
