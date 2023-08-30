package dev.zalaris.alumni.sync.app.payslip;

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

@Component("payslipFileStream")
@EnableConfigurationProperties(PayslipFileMessageStreamFunction.RedeliveryProperties.class)
@Slf4j
class PayslipFileMessageStreamFunction extends StreamMessageFunctionTemplate<PayslipFileMessage, PayslipFileMessage> {

  private final static String NAME = "payslip-file-sync-job";

  public PayslipFileMessageStreamFunction(RedeliveryProperties properties,
                                          ObjectProvider<StreamMessageSupport<PayslipFileMessage>> objectProvider,
                                          PayslipFileJobLauncher launcher) {
    super(NAME, objectProvider.getObject(properties.build(NAME)), launcher::launch);
  }

  @Override
  protected boolean isInputValid(PayslipFileMessage payload) {
    if (!StringUtils.hasText(payload.ssid())) {
      log.warn("Field 'ssid' cannot be blank: {}", payload);
      return false;
    }
    if (!StringUtils.hasText(payload.zalarisId())) {
      log.warn("Field 'zalarisId' cannot be blank: {}", payload);
      return false;
    }
    if (!StringUtils.hasText(payload.fileIdentifier())) {
      log.warn("Field 'fileIdentifier' cannot be blank: {}", payload);
      return false;
    }
    return true;
  }

  @Override
  protected void beforeJob(PayslipFileMessage payload) {
    MDC.put("correlationId", UUID.randomUUID().toString());
    MDC.put("zalarisId", payload.zalarisId());
    MDC.put("ssid", payload.ssid());
    MDC.put("fileIdentifier", payload.fileIdentifier());
  }

  @Override
  protected void afterJob(PayslipFileMessage payload) {
    MDC.clear();
  }

  @ConfigurationProperties(prefix = "alumni.sync.stream.function.payslip-file.redelivery")
  static class RedeliveryProperties extends RedeliveryConfigurationProperties {}
}
