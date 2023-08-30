package dev.zalaris.alumni.app.payroll.adapter.data.mongo;

import dev.zalaris.alumni.app.infra.dataorigin.DataOrigin;
import dev.zalaris.alumni.app.employee.adapter.data.FindEmployeeSingleDataAccess;
import dev.zalaris.alumni.app.payroll.adapter.data.FindPayrollResultSingleDataAccess;
import dev.zalaris.alumni.common.domain.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
class FindPayrollResultMongoAccess implements FindPayrollResultSingleDataAccess {

  // TODO: make dependency on employee module ... leaking abstraction. Come back to this issue later.
  private final FindEmployeeSingleDataAccess findEmployeeMongoAccess;

  @Override
  public Optional<Employee> findBySsidAndZalarisId(String ssid, String zalarisId) {
    return findEmployeeMongoAccess.findBySsidAndZalarisId(ssid, zalarisId);
  }

  @Override
  public boolean supports(DataOrigin delimiter) {
    return DataOrigin.ALUMNI.equals(delimiter);
  }
}
