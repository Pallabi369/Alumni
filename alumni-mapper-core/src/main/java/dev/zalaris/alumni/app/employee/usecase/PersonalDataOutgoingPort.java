package dev.zalaris.alumni.app.employee.usecase;

import dev.zalaris.alumni.app.employee.domain.PersonalDataModel;
import dev.zalaris.alumni.common.domain.Employee;

public interface PersonalDataOutgoingPort {

  PersonalDataModel handle(Employee employee);
}
