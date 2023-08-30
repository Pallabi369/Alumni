package dev.zalaris.alumni.app.employee.adapter.data.sap;

import dev.zalaris.alumni.app.infra.dataorigin.DataOrigin;
import dev.zalaris.alumni.app.employee.adapter.data.FindEmploymentHistorySingleDataAccess;
import dev.zalaris.alumni.common.domain.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
class FindEmploymentSapAccess implements FindEmploymentHistorySingleDataAccess {

  private final FindEmployeeSapAccess findEmployeeSapAccess;

  @Override
  public List<Employee> findBySsid(String ssid) {
    log.error("Unable to get data from SAP with only ssid. Misconfiguration detected");
    throw new UnsupportedOperationException();
  }

  @Override
  public List<Employee> findByZalarisId(String zalarisId) {
    return findEmployeeSapAccess.findBySsidAndZalarisId(null, zalarisId)
      .map(List::of)
      .orElse(List.of());
  }

  @Override
  public boolean supports(DataOrigin delimiter) {
    return DataOrigin.SAP_API.equals(delimiter);
  }
}
