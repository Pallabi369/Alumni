package dev.zalaris.alumni.app.infra.logging;

import dev.zalaris.alumni.app.infra.access.ClaimAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static dev.zalaris.alumni.app.infra.access.ClaimAccessor.SSID;
import static dev.zalaris.alumni.app.infra.access.ClaimAccessor.ZALARIS_ID;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class MDCInterceptor implements HandlerInterceptor {

  private final ClaimAccessor claimAccessor;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler) throws Exception {
    MDC.put("correlationId", UUID.randomUUID().toString());
    MDC.put("requesterZalarisId", claimAccessor.<String>get(ZALARIS_ID).orElse(null));
    MDC.put("requesterSsid", claimAccessor.<String>get(SSID).orElse(null));

    log.info("Request [{}] {}, params: {}", request.getMethod(), request.getRequestURI(), request.getQueryString());
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response,
                         Object handler, ModelAndView modelAndView) throws Exception {
    log.info("Request completed [{}] {}", request.getMethod(), request.getRequestURI());
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                              Object handler, Exception ex) throws Exception {
    MDC.clear();
  }
}
