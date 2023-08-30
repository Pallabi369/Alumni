package dev.zalaris.alumni.app.employee.adapter.web;

import dev.zalaris.alumni.app.employee.adapter.mapper.EmployeeMapper;
import dev.zalaris.alumni.app.employee.domain.EmploymentHistoryModel;
import dev.zalaris.alumni.app.employee.usecase.EmploymentHistoryOutgoingPort;
import dev.zalaris.alumni.common.domain.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
class EmploymentPresenter implements EmploymentHistoryOutgoingPort {

  private final EmployeeMapper mapper;

  @Override
  public EmploymentHistoryModel handle(String ssid, List<Employee> employees) {
    var result = mapper.mapToEmploymentHistoryModel(ssid, employees);
    log.debug("Employments of ssid:{} mapped to EmploymentHistoryModel successfully", ssid);
    return result;
  }
}
