package dev.zalaris.alumni.app.employee.adapter.data;

import dev.zalaris.alumni.common.domain.Employee;
import org.springframework.lang.Nullable;

import java.util.Optional;

public interface FindEmployeeDataAccess {

  Optional<Employee> find(@Nullable String ssid, String zalarisId);

}
