package dev.zalaris.alumni.app.payroll.adapter.data.sap;

import dev.zalaris.alumni.app.infra.dataorigin.DataOrigin;
import dev.zalaris.alumni.app.payroll.adapter.data.FindPayrollResultSingleDataAccess;
import dev.zalaris.alumni.common.domain.Employee;
import dev.zalaris.alumni.common.sap.FetchPayrollResults;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
class FindPayrollResultSapAccess implements FindPayrollResultSingleDataAccess {

  private final FetchPayrollResults fetchPayrollResults;

  @Override
  public Optional<Employee> findBySsidAndZalarisId(String ssid, String zalarisId) {
    if (ssid != null) {
      log.error("The narrowing ssid must not be set. It's current value: {}", ssid);
      throw new UnsupportedOperationException();
    }
    var payrollResults = fetchPayrollResults.fetch(zalarisId);
    if (payrollResults != null) {
      return Optional.of(new Employee(zalarisId, payrollResults));
    }
    return Optional.empty();
  }

  @Override
  public boolean supports(DataOrigin delimiter) {
    return DataOrigin.SAP_API.equals(delimiter);
  }

}
