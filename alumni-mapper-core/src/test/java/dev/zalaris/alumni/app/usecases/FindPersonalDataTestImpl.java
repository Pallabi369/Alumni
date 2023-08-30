package dev.zalaris.alumni.app.usecases;

import dev.zalaris.alumni.app.employee.domain.PersonalDataModel;
import dev.zalaris.alumni.app.employee.usecase.FindPersonalDataUseCase;

class FindPersonalDataTestImpl implements FindPersonalDataUseCase {
  @Override
  public PersonalDataModel find(String ssid, String zalarisId) {
    return new PersonalDataModel(ssid, zalarisId, null, null,
      null, null, null, null, null, null);
  }
}
