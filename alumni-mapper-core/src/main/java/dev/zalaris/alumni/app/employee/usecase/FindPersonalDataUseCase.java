package dev.zalaris.alumni.app.employee.usecase;

import dev.zalaris.alumni.app.infra.access.EmployeeAccess;
import dev.zalaris.alumni.app.employee.domain.PersonalDataModel;
import org.springframework.lang.Nullable;

public interface FindPersonalDataUseCase {

  @EmployeeAccess
  PersonalDataModel find(@Nullable String ssid, String zalarisId);

}
