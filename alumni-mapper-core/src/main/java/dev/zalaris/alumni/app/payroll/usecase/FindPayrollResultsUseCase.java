package dev.zalaris.alumni.app.payroll.usecase;

import dev.zalaris.alumni.app.infra.access.EmployeeAccess;
import dev.zalaris.alumni.app.payroll.domain.PayrollResultsModel;
import org.springframework.lang.Nullable;

public interface FindPayrollResultsUseCase {

  @EmployeeAccess
  PayrollResultsModel find(@Nullable String ssid, String zalarisId);

}
