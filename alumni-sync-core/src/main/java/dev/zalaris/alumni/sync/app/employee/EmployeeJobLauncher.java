package dev.zalaris.alumni.sync.app.employee;

import dev.zalaris.alumni.common.domain.Employee;
import dev.zalaris.alumni.sync.app.employee.port.outbound.EmployeeProvider;
import dev.zalaris.alumni.sync.app.employee.port.outbound.EmployeeRepository;
import dev.zalaris.alumni.sync.app.payslip.port.inbound.CreatePayslipFileUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
class EmployeeJobLauncher {

  private final EmployeeProvider provider;
  private final EmployeeRepository repository;
  private final CreatePayslipFileUseCase createPayslipFileUseCase;

  public boolean launch(String jobName, EmployeeMessage message) {
    var zalarisId = message.zalarisId();
    log.info("{} has started [zalarisId:{}]", jobName, zalarisId);
    try {
      createEmployeeIfNotExists(message);

      provider.fetchMasterData(zalarisId)
        .ifPresentOrElse(masterData -> {
          repository.update(zalarisId, masterData);
          log.info("{} employee master data updated", message.zalarisId());

          var payrolls = provider.fetchPayrolls(zalarisId);
          repository.update(zalarisId, payrolls);
          log.info("{} employee payrolls data updated", message.zalarisId());

          payrolls.forEach(resultsResponse ->
            createPayslipFileUseCase.create(message.ssid(), zalarisId, resultsResponse.getSeqnr()));
        }, () -> log.info("{} employee master data not found", zalarisId));
      return true;
    } catch (Exception exception) {
      log.error("An exception occurred during employee processing", exception);
      return false;
    }
  }

  private void createEmployeeIfNotExists(EmployeeMessage message) {
    if (repository.create(new Employee(message.ssid(), message.zalarisId(), message.expirationDate()))) {
      log.info("Created the employee [ssid:{}, zalarisId:{}]", message.ssid(), message.zalarisId());
    } else {
      log.info("The employee [ssid:{}, zalarisId:{}] data will be updated", message.ssid(), message.zalarisId());
    }
  }

}
