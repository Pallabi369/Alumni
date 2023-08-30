package dev.zalaris.alumni.app.employee.usecase;

import dev.zalaris.alumni.app.employee.adapter.data.FindEmploymentHistoryDataAccess;
import dev.zalaris.alumni.app.employee.domain.EmploymentHistoryModel;
import dev.zalaris.alumni.app.employee.domain.EmploymentHistoryNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
class FindEmploymentHistoryInteractor implements FindEmploymentHistoryUseCase {

  private final FindEmploymentHistoryDataAccess findEmploymentHistoryDataAccess;
  private final EmploymentHistoryOutgoingPort employmentHistoryOutgoingPort;

  @Override
  public EmploymentHistoryModel find(String ssid, String zalarisId) {
    log.info("Requested employment history (ssid: {}, zalarisId: {})", ssid, zalarisId);
    var employments = findEmploymentHistoryDataAccess.find(ssid, zalarisId);

    if (log.isInfoEnabled()) {
      log.info("Found [{}]",
        employments.stream().map(e -> """
            {ssid: %s, zalarisId: %s, companyCode: %s}"""
              .formatted(e.getSsid(), e.getZalarisId(), e.getCompanyCodeValue()))
          .collect(Collectors.joining(",")));
    }

    if (employments.isEmpty()) {
      throw EmploymentHistoryNotFoundException.of(ssid, zalarisId);
    }
    return employmentHistoryOutgoingPort.handle(ssid, employments);
  }

}
