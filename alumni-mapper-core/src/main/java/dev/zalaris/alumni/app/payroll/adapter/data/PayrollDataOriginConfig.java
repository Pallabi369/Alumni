package dev.zalaris.alumni.app.payroll.adapter.data;

import dev.zalaris.alumni.common.sap.SapConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.plugin.core.config.EnablePluginRegistries;

@Configuration
@EnablePluginRegistries({
  FindPayslipFileSingleDataAccess.class,
  FindPayrollResultSingleDataAccess.class
})
@Import(SapConfig.class)
class PayrollDataOriginConfig {}
