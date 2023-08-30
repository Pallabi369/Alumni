package dev.zalaris.alumni.sync.app.employee;

import dev.zalaris.alumni.sync.app.employee.port.inbound.CreateEmployeeUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
class LocalCreateEmployeeService implements CreateEmployeeUseCase {

  private final EmployeeJobLauncher employeeJobLauncher;

  private final static String NAME = "local-employee-data-sync-job";

  @Override
  public void create(String ssid, String zalarisId, LocalDateTime expirationDate) {
    employeeJobLauncher.launch(NAME, new EmployeeMessage(ssid, zalarisId, expirationDate));
  }

}
