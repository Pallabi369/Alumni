package dev.zalaris.alumni.app.payroll.adapter.data.sap;

import dev.zalaris.alumni.app.employee.domain.PayslipNotFoundException;
import dev.zalaris.alumni.app.infra.dataorigin.DataOrigin;
import dev.zalaris.alumni.app.payroll.adapter.data.FindPayslipFileSingleDataAccess;
import dev.zalaris.alumni.app.payroll.domain.PayslipFile;
import dev.zalaris.alumni.common.sap.FetchPayslipFile;
import dev.zalaris.alumni.common.sap.PayslipDocumentNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
class FindPayslipFileSapAccess implements FindPayslipFileSingleDataAccess {

  private final FetchPayslipFile fetchPayslipFile;

  @Override
  public Optional<PayslipFile> getFile(String ssid, String zalarisId, String sequenceId) {
    if (ssid != null) {
      log.error("The narrowing ssid must not be set. It's current value: {}", ssid);
      throw new UnsupportedOperationException();
    }

    try {
      var inputStream = fetchPayslipFile.fetch(zalarisId, sequenceId);
      return Optional.of(new PayslipFile(null, zalarisId, inputStream));
    } catch(PayslipDocumentNotFoundException exception) {
      throw PayslipNotFoundException.of(exception.getName(), exception.getSequenceId());
    }

  }

  @Override
  public boolean supports(DataOrigin delimiter) {
    return DataOrigin.SAP_API.equals(delimiter);
  }
}
