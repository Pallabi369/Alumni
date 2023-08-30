package dev.zalaris.alumni.app.usecases;

import dev.zalaris.alumni.app.payroll.domain.PayrollResultsModel;
import dev.zalaris.alumni.app.payroll.usecase.FindPayrollResultsUseCase;

import java.util.List;

class FindPayrollResultsTestImpl implements FindPayrollResultsUseCase {
  @Override
  public PayrollResultsModel find(String ssid, String zalarisId) {
    return new PayrollResultsModel(ssid, zalarisId, List.of());
  }
}
