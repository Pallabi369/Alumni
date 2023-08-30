package dev.zalaris.alumni.app.usecases;

import dev.zalaris.alumni.app.infra.access.AccessConfig;
import dev.zalaris.alumni.app.infra.access.ClaimAccessor;
import dev.zalaris.alumni.app.employee.usecase.FindEmploymentHistoryUseCase;
import dev.zalaris.alumni.app.employee.usecase.FindPersonalDataUseCase;
import dev.zalaris.alumni.app.payroll.usecase.FindPayrollResultsUseCase;
import dev.zalaris.alumni.app.payroll.usecase.FindPayslipFileUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {AccessConfig.class, FindPersonalDataTestImpl.class,
  FindPayslipFileTestImpl.class, FindPayrollResultsTestImpl.class, FindEmploymentHistoryTestImpl.class})
@Import(UseCasesSecurityTests.Config.class)
@ExtendWith(SpringExtension.class)
class UseCasesSecurityTests {

  @EnableGlobalMethodSecurity(prePostEnabled = true)
  protected static class Config extends GlobalMethodSecurityConfiguration {}

  @Autowired FindPersonalDataUseCase findPersonalDataUseCase;
  @Autowired FindPayslipFileUseCase findPayslipFileUseCase;
  @Autowired FindPayrollResultsUseCase findPayrollResultsUseCase;
  @Autowired FindEmploymentHistoryUseCase findEmploymentHistoryUseCase;
  @MockBean ClaimAccessor claimAccessor;

  @Test
  @WithMockUser(username = "user")
  void whenNoZalarisIdNorSsid_expectDenied() {
    assertThrows(AccessDeniedException.class, () -> findPersonalDataUseCase.find("*", "*"));
    assertThrows(AccessDeniedException.class, () -> findPayslipFileUseCase.find("*", "*", "*"));
    assertThrows(AccessDeniedException.class, () -> findPayrollResultsUseCase.find("*", "*"));
    assertThrows(AccessDeniedException.class, () -> findEmploymentHistoryUseCase.find("*", "*"));
  }

  @Test
  @WithMockUser(username = "user")
  void whenZalarisMatches_expectOk() {
    when(claimAccessor.get(ClaimAccessor.ZALARIS_ID)).thenReturn(Optional.of("zalarisId"));
    findPersonalDataUseCase.find("*", "zalarisId");
    findPayslipFileUseCase.find("*", "zalarisId", "*");
    findPayrollResultsUseCase.find("*", "zalarisId");
    findEmploymentHistoryUseCase.find(null, "zalarisId");
  }

  @Test
  @WithMockUser(username = "user")
  void whenZalarisMatchesButSsidNot_expectDenied() {
    when(claimAccessor.get(ClaimAccessor.ZALARIS_ID)).thenReturn(Optional.of("zalarisId"));
    when(claimAccessor.get(ClaimAccessor.SSID)).thenReturn(Optional.of("ssid"));
    assertThrows(AccessDeniedException.class, () -> findPersonalDataUseCase.find("*", "zalarisId"));
    assertThrows(AccessDeniedException.class, () -> findPayslipFileUseCase.find("*", "zalarisId", "*"));
    assertThrows(AccessDeniedException.class, () -> findPayrollResultsUseCase.find("*", "zalarisId"));
    assertThrows(AccessDeniedException.class, () -> findEmploymentHistoryUseCase.find("*", null));
  }

  @Test
  @WithMockUser(username = "user")
  void whenZalarisNorSsidMatches_expectDenied() {
    when(claimAccessor.get(ClaimAccessor.ZALARIS_ID)).thenReturn(Optional.of("zalarisId"));
    when(claimAccessor.get(ClaimAccessor.SSID)).thenReturn(Optional.of("ssid"));
    assertThrows(AccessDeniedException.class, () -> findPersonalDataUseCase.find("*", "*"));
    assertThrows(AccessDeniedException.class, () -> findPayslipFileUseCase.find("*", "*", "*"));
    assertThrows(AccessDeniedException.class, () -> findPayrollResultsUseCase.find("*", "*"));
  }

  @Test
  @WithMockUser(username = "user")
  void whenSsidMatches_expectOk() {
    when(claimAccessor.get(ClaimAccessor.SSID)).thenReturn(Optional.of("ssid"));
    findPersonalDataUseCase.find("ssid", "*");
    findPayslipFileUseCase.find("ssid", "*", "*");
    findPayrollResultsUseCase.find("ssid", "*");
    findEmploymentHistoryUseCase.find("ssid", null);
  }

  @Test
  @WithMockUser(username = "user")
  void whenSsidNotMatches_expectOk() {
    when(claimAccessor.get(ClaimAccessor.SSID)).thenReturn(Optional.of("ssid"));
    assertThrows(AccessDeniedException.class, () -> findPersonalDataUseCase.find("*", "*"));
    assertThrows(AccessDeniedException.class, () -> findPayslipFileUseCase.find("*", "*", "*"));
    assertThrows(AccessDeniedException.class, () -> findPayrollResultsUseCase.find("*", "*"));
    assertThrows(AccessDeniedException.class, () -> findPayrollResultsUseCase.find("*", null));
  }

}
