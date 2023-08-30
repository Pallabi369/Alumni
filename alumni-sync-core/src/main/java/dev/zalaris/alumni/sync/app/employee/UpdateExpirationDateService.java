package dev.zalaris.alumni.sync.app.employee;

import dev.zalaris.alumni.sync.app.employee.port.inbound.UpdateExpirationDateUseCase;
import dev.zalaris.alumni.sync.app.employee.port.outbound.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
class UpdateExpirationDateService implements UpdateExpirationDateUseCase {
  private final EmployeeRepository repository;

  @Override
  public void updateDateExpiration(String zalarisId, LocalDateTime expirationTime) {
    repository.update(zalarisId, expirationTime);
  }
}
