package dev.zalaris.alumni.app.usecases;

import dev.zalaris.alumni.app.employee.domain.EmploymentHistoryModel;
import dev.zalaris.alumni.app.employee.usecase.FindEmploymentHistoryUseCase;

import java.util.List;

class FindEmploymentHistoryTestImpl implements FindEmploymentHistoryUseCase {
  @Override
  public EmploymentHistoryModel find(String ssid, String zalarisId) {
    return new EmploymentHistoryModel(ssid, List.of());
  }
}
