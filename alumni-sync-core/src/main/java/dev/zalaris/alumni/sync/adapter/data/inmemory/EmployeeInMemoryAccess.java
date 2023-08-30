package dev.zalaris.alumni.sync.adapter.data.inmemory;

import com.zalaris.api.nonsap.DTPAMasterdataResponse;
import com.zalaris.api.nonsap.PayrollResult;
import dev.zalaris.alumni.common.domain.Employee;
import dev.zalaris.alumni.sync.app.employee.EmployeeNotFoundException;
import dev.zalaris.alumni.sync.app.employee.port.outbound.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
class EmployeeInMemoryAccess implements EmployeeRepository {

  private final Map<String, Employee> inMemoryStore;

  @Override
  public boolean create(Employee employee) {
    log.info("Employee ssid:{}, zalarisId:{} created in in-memory store",
      employee.getSsid(), employee.getZalarisId());

    if (inMemoryStore.put(employee.getZalarisId(), employee) == null) {
      return true;
    }
    return false;
  }

  @Override
  public void update(String zalarisId, DTPAMasterdataResponse personalDataContainer) {
    log.info("Employee zalarisId:{} updated in in-memory store", zalarisId);
    var employee = inMemoryStore.get(zalarisId);
    if (employee == null) {
      throw new EmployeeNotFoundException("""
        Employee %s not found""".formatted(zalarisId));
    }
    employee.setPersonalDataContainer(personalDataContainer);
  }

  @Override
  public void update(String zalarisId, List<PayrollResult> payrollResultsList) {
    log.info("Employee zalarisId:{} updated in in-memory store", zalarisId);
    var employee = inMemoryStore.get(zalarisId);
    if (employee == null) {
      throw new EmployeeNotFoundException("""
        Employee %s not found""".formatted(zalarisId));
    }
    employee.setPayrollResults(payrollResultsList);
  }

  @Override
  public void update(String zalarisId, LocalDateTime expirationDate) {
    log.info("Employee zalarisId:{} updated in in-memory store", zalarisId);
    var employee = inMemoryStore.get(zalarisId);
    if (employee == null) {
      throw new EmployeeNotFoundException("""
        Employee %s not found""".formatted(zalarisId));
    }
    employee.setExpirationDate(expirationDate);
  }
}
