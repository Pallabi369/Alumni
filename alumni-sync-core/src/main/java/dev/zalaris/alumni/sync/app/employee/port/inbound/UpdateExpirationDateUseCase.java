package dev.zalaris.alumni.sync.app.employee.port.inbound;


import java.time.LocalDateTime;

public interface UpdateExpirationDateUseCase {

  void updateDateExpiration(String zalarisId, LocalDateTime expirationTime);

}
