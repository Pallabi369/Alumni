package dev.zalaris.alumni.sync.app.employee.port.inbound;

import java.time.LocalDateTime;

public interface CreateEmployeeUseCase {

  void create(String ssid, String zalarisId, LocalDateTime expirationDate);

}
