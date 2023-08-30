package dev.zalaris.alumni.sync.app.employee;

import dev.zalaris.alumni.sync.common.RedeliveryConfigurationProperties;
import dev.zalaris.sync.StreamMessageFunctionTemplate;
import dev.zalaris.sync.StreamMessageSupport;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Component("employeeStream")
@EnableConfigurationProperties(EmployeeMessageStreamFunction.RedeliveryProperties.class)
@Slf4j
class EmployeeMessageStreamFunction extends StreamMessageFunctionTemplate<EmployeeMessage, EmployeeMessage> {

  private final static String NAME = "employee-data-sync-job";

  public EmployeeMessageStreamFunction(RedeliveryProperties properties,
                                       ObjectProvider<StreamMessageSupport<EmployeeMessage>> objectProvider,
                                       EmployeeJobLauncher launcher) {
    super(NAME, objectProvider.getObject(properties.build(NAME)), launcher::launch);
  }

  @Override
  protected boolean isInputValid(EmployeeMessage payload) {
    if (!StringUtils.hasText(payload.ssid())) {
      log.warn("Field 'ssid' cannot be blank: {}", payload);
      return false;
    }
    if (!StringUtils.hasText(payload.zalarisId())) {
      log.warn("Field 'zalarisId' cannot be blank: {}", payload);
      return false;
    }
    if (payload.expirationDate() == null) {
      log.warn("Field 'expirationDate' cannot be null: {}", payload);
      return false;
    }
    return true;
  }

  @Override
  protected void beforeJob(EmployeeMessage payload) {
    MDC.put("correlationId", UUID.randomUUID().toString());
    MDC.put("zalarisId", payload.zalarisId());
    MDC.put("ssid", payload.ssid());
  }

  @Override
  protected void afterJob(EmployeeMessage payload) {
    MDC.clear();
  }

  @ConfigurationProperties(prefix = "alumni.sync.stream.function.employee.redelivery")
  static class RedeliveryProperties extends RedeliveryConfigurationProperties {}
}
