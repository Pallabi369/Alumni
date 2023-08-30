package dev.zalaris.alumni.app.usecases;

import dev.zalaris.alumni.app.payroll.domain.PayslipFile;
import dev.zalaris.alumni.app.payroll.usecase.FindPayslipFileUseCase;
import org.springframework.util.StreamUtils;

class FindPayslipFileTestImpl implements FindPayslipFileUseCase {
  @Override
  public PayslipFile find(String ssid, String zalarisId, String sequenceId) {
    return new PayslipFile(ssid, zalarisId, StreamUtils.emptyInput());
  }
}
