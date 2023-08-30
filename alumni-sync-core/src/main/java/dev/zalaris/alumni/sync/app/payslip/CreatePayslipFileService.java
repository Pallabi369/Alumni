package dev.zalaris.alumni.sync.app.payslip;

import dev.zalaris.alumni.sync.app.payslip.port.inbound.CreatePayslipFileUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;

@Slf4j
@RequiredArgsConstructor
class CreatePayslipFileService implements CreatePayslipFileUseCase {

  private final StreamBridge streamBridge;

  private static final String BINDING_NAME = "payslipFileStream-out-0";

  @Override
  public void create(String ssid, String zalarisId, String fileIdentifier) {
    var message = new PayslipFileMessage(ssid, zalarisId, fileIdentifier);
    try {
      var isSent = streamBridge.send(BINDING_NAME, message);
      if (!isSent) {
        log.warn("Payslip file sync request {} was undelivered", message);
        throw new PayslipFileMessageDeliveryException(message);
      } else {
        log.info("Payslip file sync request {} delivered successfully", message);
      }
    } catch (Exception exception) {
      log.warn("Payslip file sync request {} was undelivered", message, exception);
      throw new PayslipFileMessageDeliveryException(message, exception);
    }
  }

}
