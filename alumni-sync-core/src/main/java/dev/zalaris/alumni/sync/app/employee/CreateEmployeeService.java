package dev.zalaris.alumni.sync.app.employee;

import dev.zalaris.alumni.sync.app.employee.port.inbound.CreateEmployeeUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
class CreateEmployeeService implements CreateEmployeeUseCase {

  private final StreamBridge streamBridge;

  private static final String BINDING_NAME = "employeeStream-out-0";

  @Override
  public void create(String ssid, String zalarisId, LocalDateTime expirationDate) {
    var msg = new EmployeeMessage(ssid, zalarisId, expirationDate);
    try {
      var isSent = streamBridge.send(BINDING_NAME, msg);
      if (!isSent) {
        log.warn("Sync request {} was undelivered", msg);
        throw new EmployeeMessageDeliveryException(zalarisId);
      } else {
        log.info("Sync request {} delivered successfully", msg);
      }
    } catch (Exception exception) {
      log.warn("Sync request {} was undelivered", msg, exception);
      throw new EmployeeMessageDeliveryException(zalarisId, exception);
    }
  }

}
