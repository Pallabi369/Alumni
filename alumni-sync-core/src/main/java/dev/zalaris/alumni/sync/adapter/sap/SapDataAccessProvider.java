package dev.zalaris.alumni.sync.adapter.sap;

import com.zalaris.api.nonsap.DTPAMasterdataResponse;
import com.zalaris.api.nonsap.PayrollResult;
import dev.zalaris.alumni.common.sap.FetchPayrollResults;
import dev.zalaris.alumni.common.sap.FetchPayslipFile;
import dev.zalaris.alumni.common.sap.FetchPersonalDataContainer;
import dev.zalaris.alumni.sync.app.employee.port.outbound.EmployeeProvider;
import dev.zalaris.alumni.sync.app.payslip.port.outbound.PayslipFileProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
class SapDataAccessProvider implements EmployeeProvider, PayslipFileProvider {

  private final FetchPersonalDataContainer fetchPersonalDataContainer;
  private final FetchPayrollResults fetchPayrollResults;
  private final FetchPayslipFile fetchPayslipFile;

  @Override
  public Optional<DTPAMasterdataResponse> fetchMasterData(String zalarisId) {
    return fetchPersonalDataContainer.fetch(zalarisId);
  }

  @Override
  public List<PayrollResult> fetchPayrolls(String zalarisId) {
    return fetchPayrollResults.fetch(zalarisId);
  }

  @Override
  public InputStream fetchFileInputStream(String zalarisId, String fileIdentifier) {
    return fetchPayslipFile.fetch(zalarisId, fileIdentifier);
  }

}
