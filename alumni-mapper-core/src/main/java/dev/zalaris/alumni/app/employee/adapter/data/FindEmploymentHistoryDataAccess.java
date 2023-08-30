package dev.zalaris.alumni.app.employee.adapter.data;

import dev.zalaris.alumni.common.domain.Employee;
import org.springframework.lang.Nullable;

import java.util.List;

public interface FindEmploymentHistoryDataAccess {

  List<Employee> find(@Nullable String ssid, @Nullable String zalarisId);

}
