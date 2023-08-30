package dev.zalaris.alumni.app.employee.usecase;

import dev.zalaris.alumni.app.employee.adapter.data.FindEmployeeDataAccess;
import dev.zalaris.alumni.app.employee.domain.EmployeeNotFoundException;
import dev.zalaris.alumni.app.employee.domain.PersonalDataModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
class FindPersonalDataInteractor implements FindPersonalDataUseCase {

  private final FindEmployeeDataAccess findEmployeeDataAccess;
  private final PersonalDataOutgoingPort personalDataOutgoingPort;

  @Override
  public PersonalDataModel find(String ssid, String zalarisId) {
    log.info("Requested employee data of {} (narrowing ssid: {})", zalarisId, ssid != null ? ssid : "none");
    var employee = findEmployeeDataAccess.find(ssid, zalarisId)
      .orElseThrow(() -> EmployeeNotFoundException.of(zalarisId));
    log.info("Found employee: [ssid:{}, zalarisId:{}}", employee.getSsid(), employee.getZalarisId());
    return personalDataOutgoingPort.handle(employee);
  }
}
