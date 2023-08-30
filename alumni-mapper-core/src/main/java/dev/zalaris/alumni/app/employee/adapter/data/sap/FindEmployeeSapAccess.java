package dev.zalaris.alumni.app.employee.adapter.data.sap;

import dev.zalaris.alumni.app.employee.adapter.data.FindEmployeeSingleDataAccess;
import dev.zalaris.alumni.app.infra.dataorigin.DataOrigin;
import dev.zalaris.alumni.common.domain.Employee;
import dev.zalaris.alumni.common.sap.FetchPersonalDataContainer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
@Slf4j
class FindEmployeeSapAccess implements FindEmployeeSingleDataAccess {

  private final FetchPersonalDataContainer fetchPersonalDataContainer;

  @Override
  public boolean supports(DataOrigin dataOrigin) {
    return DataOrigin.SAP_API.equals(dataOrigin);
  }

  @Override
  public Optional<Employee> findBySsidAndZalarisId(String ssid, String zalarisId) {
    if (ssid != null) {
      log.error("The narrowing ssid must not be set. It's current value: {}", ssid);
      throw new UnsupportedOperationException();
    }
    log.debug("Requested data of {} from SAP API", zalarisId);
    return fetchPersonalDataContainer.fetch(zalarisId)
      .map(personalDataContainer -> new Employee(zalarisId, personalDataContainer));
  }
}
