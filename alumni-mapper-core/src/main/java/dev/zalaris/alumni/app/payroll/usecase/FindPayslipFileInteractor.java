package dev.zalaris.alumni.app.payroll.usecase;

import dev.zalaris.alumni.app.employee.domain.PayslipNotFoundException;
import dev.zalaris.alumni.app.payroll.adapter.data.FindPayslipFileDataAccess;
import dev.zalaris.alumni.app.payroll.domain.PayslipFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
class FindPayslipFileInteractor implements FindPayslipFileUseCase {

  private final FindPayslipFileDataAccess findPayslipFileDataAccess;

  @Override
  public PayslipFile find(String ssid, String zalarisId, String sequenceId) {
    log.info("Requested payslip of {} (narrowing ssid: {})", zalarisId, ssid != null ? ssid : "none");
    var payslip = findPayslipFileDataAccess.getFile(ssid, zalarisId, sequenceId)
      .orElseThrow(() -> PayslipNotFoundException.of(zalarisId, sequenceId));
    log.info("Found payslip of ssid:{}, zalarisId:{}", payslip.ssid(), payslip.zalarisId());
    return payslip;
  }
}
