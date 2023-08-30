package dev.zalaris.alumni.app.employee.adapter.data;

import dev.zalaris.alumni.app.infra.dataorigin.DataOrigin;
import dev.zalaris.alumni.common.domain.Employee;
import org.springframework.plugin.core.Plugin;

import java.util.List;

public interface FindEmploymentHistorySingleDataAccess extends Plugin<DataOrigin> {

  List<Employee> findBySsid(String ssid);

  List<Employee> findByZalarisId(String zalarisId);

}
