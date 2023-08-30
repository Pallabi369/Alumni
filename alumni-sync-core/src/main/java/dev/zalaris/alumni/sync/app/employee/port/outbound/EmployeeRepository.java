package dev.zalaris.alumni.sync.app.employee.port.outbound;

import com.zalaris.api.nonsap.DTPAMasterdataResponse;
import com.zalaris.api.nonsap.PayrollResult;
import dev.zalaris.alumni.common.domain.Employee;

import java.time.LocalDateTime;
import java.util.List;

public interface EmployeeRepository {

  boolean create(Employee employee);

  void update(String zalarisId, DTPAMasterdataResponse personalDataContainer);

  void update(String zalarisId, List<PayrollResult> payrollResultsList);

  void update(String zalarisId, LocalDateTime date);

}
