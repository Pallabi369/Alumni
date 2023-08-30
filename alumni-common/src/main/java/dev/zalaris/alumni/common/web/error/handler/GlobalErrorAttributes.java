package dev.zalaris.alumni.common.web.error.handler;

import dev.zalaris.alumni.common.web.error.Error;
import dev.zalaris.alumni.common.web.error.ErrorResponse;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

import static dev.zalaris.alumni.common.web.error.ErrorCode.REST;

public class GlobalErrorAttributes extends DefaultErrorAttributes {

  @Override
  public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
    var errorAttributes = super.getErrorAttributes(webRequest, options);
    var errorResponse = new ErrorResponse(
      (Integer) errorAttributes.get("status"),
      (String) errorAttributes.get("path"),
      new Error(REST, (String) errorAttributes.get("error")));
    return errorResponse.toAttributeMap();
  }
}
