package dev.zalaris.alumni.sync.app.payslip;

import dev.zalaris.alumni.sync.app.payslip.port.inbound.CreatePayslipFileUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

@Slf4j
@RequiredArgsConstructor
class LocalCreatePayslipFileService implements CreatePayslipFileUseCase {

  private final PayslipFileJobLauncher synchronizePayslipFileUseCase;

  private static final String JOB_NAME = "local-payslip-file-sync-job";

  @Override @Async
  public void create(String ssid, String zalarisId, String fileIdentifier) {
    synchronizePayslipFileUseCase.launch(JOB_NAME, new PayslipFileMessage(ssid, zalarisId, fileIdentifier));
  }

}
