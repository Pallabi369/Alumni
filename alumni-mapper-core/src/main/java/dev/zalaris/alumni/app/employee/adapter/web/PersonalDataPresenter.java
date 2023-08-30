package dev.zalaris.alumni.app.employee.adapter.web;

import dev.zalaris.alumni.app.employee.adapter.mapper.PersonalDataMapper;
import dev.zalaris.alumni.app.employee.domain.PersonalDataModel;
import dev.zalaris.alumni.app.employee.usecase.PersonalDataOutgoingPort;
import dev.zalaris.alumni.common.domain.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
class PersonalDataPresenter implements PersonalDataOutgoingPort {

  private final PersonalDataMapper mapper;

  @Override
  public PersonalDataModel handle(Employee employee) {
    var result = mapper.mapToPersonalDataModel(employee);
    log.debug("Employee zalarisId:{} mapped to PersonalDataModel successfully", employee.getZalarisId());
    return result;
  }
}
