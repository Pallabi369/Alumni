package dev.zalaris.alumni.sync.app.payslip;

import dev.zalaris.alumni.sync.app.payslip.port.inbound.CreatePayslipFileUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.stream.config.BindingServiceConfiguration;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(BindingServiceConfiguration.class)
@Slf4j
class PayslipFileConfiguration {

  @Bean
  @ConditionalOnBean(StreamBridge.class)
  CreatePayslipFileUseCase createPayslipFileSyncRequestUseCase(StreamBridge streamBridge) {
    log.info("CreatePayslipFileService created");
    return new CreatePayslipFileService(streamBridge);
  }

  @Bean
  @ConditionalOnMissingBean
  CreatePayslipFileUseCase localCreatePayslipFileSyncRequestUseCase(PayslipFileJobLauncher payslipFileJobLauncher) {
    log.info("LocalCreatePayslipFileService created (skipping ServiceBus broker)");
    return new LocalCreatePayslipFileService(payslipFileJobLauncher);
  }

}
