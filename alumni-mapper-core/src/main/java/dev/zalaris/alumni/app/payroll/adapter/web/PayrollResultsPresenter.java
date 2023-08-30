package dev.zalaris.alumni.app.payroll.adapter.web;

import dev.zalaris.alumni.app.payroll.adapter.mapper.PayrollResultMapper;
import dev.zalaris.alumni.app.payroll.domain.PayrollResultsModel;
import dev.zalaris.alumni.app.payroll.usecase.PayrollResultsOutgoingPort;
import dev.zalaris.alumni.common.domain.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PayrollResultsPresenter implements PayrollResultsOutgoingPort {

  private final PayrollResultMapper mapper;

  @Override
  public PayrollResultsModel handle(Employee employee) {
    var result = mapper.mapToPayrollResultsModel(
      employee.getSsid(), employee.getZalarisId(), employee.getPayrollResults());
    log.debug("Employee zalarisId:{} mapped to PayrollResultsModel successfully", employee.getZalarisId());
    return result;
  }
}
