package dev.zalaris.alumni.sync.adapter.sap;

import dev.zalaris.alumni.common.sap.FetchPayrollResults;
import dev.zalaris.alumni.common.sap.FetchPayslipFile;
import dev.zalaris.alumni.common.sap.FetchPersonalDataContainer;
import dev.zalaris.alumni.common.sap.SapConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SapConfig.class)
class SapAccessConfiguration {

  @Bean
  SapDataAccessProvider employeeSapAccess(FetchPersonalDataContainer fetchPersonalDataContainer,
                                          FetchPayrollResults fetchPayrollResults, FetchPayslipFile fetchPayslipFile) {
    return new SapDataAccessProvider(fetchPersonalDataContainer, fetchPayrollResults, fetchPayslipFile);
  }
}
