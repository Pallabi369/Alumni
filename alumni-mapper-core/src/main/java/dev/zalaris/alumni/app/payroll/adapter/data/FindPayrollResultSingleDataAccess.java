package dev.zalaris.alumni.app.payroll.adapter.data;

import dev.zalaris.alumni.app.infra.dataorigin.DataOrigin;
import dev.zalaris.alumni.common.domain.Employee;
import org.springframework.lang.Nullable;
import org.springframework.plugin.core.Plugin;

import java.util.Optional;

public interface FindPayrollResultSingleDataAccess extends Plugin<DataOrigin> {

  Optional<Employee> findBySsidAndZalarisId(@Nullable String ssid, String zalarisId);

}
