package dev.zalaris.alumni.app.employee.adapter.web;

import dev.zalaris.alumni.app.infra.access.ClaimAccessor;
import dev.zalaris.alumni.app.employee.domain.EmploymentHistoryModel;
import dev.zalaris.alumni.app.employee.domain.PersonalDataModel;
import dev.zalaris.alumni.app.employee.usecase.FindEmploymentHistoryUseCase;
import dev.zalaris.alumni.app.employee.usecase.FindPersonalDataUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;

import static dev.zalaris.alumni.app.infra.access.ClaimAccessor.SSID;
import static dev.zalaris.alumni.app.infra.access.ClaimAccessor.ZALARIS_ID;

@RestController
@RequestMapping(path = "/api/employees")
@RequiredArgsConstructor
@Tag(name = "Employee")
@Slf4j
@Validated
class EmployeeController {

  private final FindEmploymentHistoryUseCase findEmploymentHistoryUseCase;
  private final FindPersonalDataUseCase findPersonalDataUseCase;
  private final ClaimAccessor claimAccessor;

  @GetMapping(path = "/{zalarisId}/personal-data")
  PersonalDataModel getPersonalData(@PathVariable @Pattern(regexp = "\\d{3}-\\d{8}") String zalarisId) {
    return findPersonalDataUseCase.find(claimAccessor.<String>get(SSID).orElse(null), zalarisId);
  }

  @GetMapping(path = "/employment/history")
  EmploymentHistoryModel getEmploymentHistory(@RequestParam(required = false) @Length(max = 64) String ssid,
                                              @RequestParam(required = false) @Pattern(regexp = "\\d{3}-\\d{8}") String zalarisId) {
    if (claimAccessor.get(SSID).isPresent()) {
      zalarisId = null;
    }
    if (claimAccessor.get(ZALARIS_ID).isPresent()) {
      ssid = null;
    }
    return findEmploymentHistoryUseCase.find(ssid, zalarisId);
  }

}
