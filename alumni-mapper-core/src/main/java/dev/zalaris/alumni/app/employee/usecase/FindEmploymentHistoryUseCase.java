package dev.zalaris.alumni.app.employee.usecase;

import dev.zalaris.alumni.app.infra.access.EmployeeHistoryAccess;
import dev.zalaris.alumni.app.employee.domain.EmploymentHistoryModel;
import org.springframework.lang.Nullable;

public interface FindEmploymentHistoryUseCase {

  @EmployeeHistoryAccess
  EmploymentHistoryModel find(@Nullable String ssid, @Nullable String zalarisId);

}
