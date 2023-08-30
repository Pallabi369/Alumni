package dev.zalaris.alumni.app.employee.adapter.data;

import dev.zalaris.alumni.common.sap.SapConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.plugin.core.config.EnablePluginRegistries;

@Configuration
@EnablePluginRegistries({
  FindEmployeeSingleDataAccess.class,
  FindEmploymentHistorySingleDataAccess.class
})
@Import(SapConfig.class)
class EmployeeDataOriginConfig {}
