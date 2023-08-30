package dev.zalaris.alumni.app.employee.usecase;

import dev.zalaris.alumni.app.employee.domain.EmploymentHistoryModel;
import dev.zalaris.alumni.common.domain.Employee;

import java.util.List;

public interface EmploymentHistoryOutgoingPort {

  EmploymentHistoryModel handle(String ssid, List<Employee> employees);

}
