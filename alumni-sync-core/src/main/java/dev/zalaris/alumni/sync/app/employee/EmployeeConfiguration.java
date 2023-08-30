package dev.zalaris.alumni.sync.app.employee;

import dev.zalaris.alumni.sync.app.employee.port.inbound.CreateEmployeeUseCase;
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
class EmployeeConfiguration {

  @Bean
  @ConditionalOnBean(StreamBridge.class)
  CreateEmployeeUseCase createSyncRequestUseCase(StreamBridge streamBridge) {
    log.info("CreateEmployeeService created");
    return new CreateEmployeeService(streamBridge);
  }

  @Bean
  @ConditionalOnMissingBean
  CreateEmployeeUseCase localCreateSyncRequestUseCase(EmployeeJobLauncher employeeJobLauncher) {
    log.info("LocalCreateEmployeeService created (skipping ServiceBus broker)");
    return new LocalCreateEmployeeService(employeeJobLauncher);
  }

}
