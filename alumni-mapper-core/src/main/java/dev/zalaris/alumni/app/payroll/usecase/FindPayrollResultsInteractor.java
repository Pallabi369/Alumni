package dev.zalaris.alumni.app.payroll.usecase;

import dev.zalaris.alumni.app.employee.domain.EmployeeNotFoundException;
import dev.zalaris.alumni.app.payroll.adapter.data.FindPayrollResultDataAccess;
import dev.zalaris.alumni.app.payroll.domain.PayrollResultsModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
class FindPayrollResultsInteractor implements FindPayrollResultsUseCase {

  private final FindPayrollResultDataAccess findPayrollResultDataAccess;
  private final PayrollResultsOutgoingPort payrollResultsOutgoingPort;

  @Override
  public PayrollResultsModel find(String ssid, String zalarisId) {
    log.info("Requested payroll results of {} (narrowing ssid: {})", zalarisId, ssid != null ? ssid : "none");
    var employee = findPayrollResultDataAccess.find(ssid, zalarisId)
      .orElseThrow(() -> EmployeeNotFoundException.of(zalarisId));
    log.info("Found {} payroll-results", employee.getPayrollResults().size());
    return payrollResultsOutgoingPort.handle(employee);
  }
}
