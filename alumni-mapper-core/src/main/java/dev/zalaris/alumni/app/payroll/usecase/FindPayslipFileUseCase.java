package dev.zalaris.alumni.app.payroll.usecase;

import dev.zalaris.alumni.app.infra.access.EmployeeAccess;
import dev.zalaris.alumni.app.payroll.domain.PayslipFile;
import org.springframework.lang.Nullable;

public interface FindPayslipFileUseCase {

  @EmployeeAccess
  PayslipFile find(@Nullable String ssid, String zalarisId, String sequenceId);

}
