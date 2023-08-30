package dev.zalaris.alumni.app.payroll.adapter.data;

import dev.zalaris.alumni.common.domain.Employee;
import org.springframework.lang.Nullable;

import java.util.Optional;

public interface FindPayrollResultDataAccess {

  Optional<Employee> find(@Nullable String ssid, String zalarisId);

}
