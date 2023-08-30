package dev.zalaris.alumni.app.employee.adapter.web;

import dev.zalaris.alumni.app.infra.access.ClaimAccessor;
import dev.zalaris.alumni.app.employee.usecase.FindEmploymentHistoryUseCase;
import dev.zalaris.alumni.app.employee.usecase.FindPersonalDataUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {EmployeeController.class})
@WebMvcTest(EmployeeController.class)
class EmployeeControllerTests {

  @MockBean FindEmploymentHistoryUseCase findEmploymentHistoryUseCase;
  @MockBean FindPersonalDataUseCase findPersonalDataUseCase;
  @MockBean ClaimAccessor claimAccessor;

  @Autowired EmployeeController employeeController;
  @Autowired MockMvc mockMvc;

  @Test
  void whenUnauthorizedRequest_expect401() throws Exception {
    this.mockMvc.perform(get("/api/employees/510-00460014/personal-data"))
      .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user")
  void whenSsidClaimExists_expectToBeIncluded() throws Exception {
    when(claimAccessor.get(ClaimAccessor.SSID)).thenReturn(Optional.of("ssid"));
    this.mockMvc.perform(get("/api/employees/510-00460014/personal-data"));
    verify(findPersonalDataUseCase).find("ssid", "510-00460014");
  }

  @Test
  @WithMockUser(username = "user")
  void whenNoQueryParams_expectNulls() throws Exception {
    this.mockMvc.perform(get("/api/employees/employment/history"));
    verify(findEmploymentHistoryUseCase).find(null, null);
  }

  @Test
  @WithMockUser(username = "user")
  void whenSsidExists_expectZalarisIdCleaned() throws Exception {
    when(claimAccessor.get(ClaimAccessor.SSID)).thenReturn(Optional.of("ssid"));
    this.mockMvc.perform(
        get("/api/employees/employment/history")
          .queryParam("ssid", "ssid")
          .queryParam("zalarisId", "510-00460014"));
    verify(findEmploymentHistoryUseCase).find("ssid", null);
  }

  @Test
  @WithMockUser(username = "user")
  void whenZalarisIdExists_expectSsidToBeCleaned() throws Exception {
    when(claimAccessor.get(ClaimAccessor.ZALARIS_ID)).thenReturn(Optional.of("111"));
    this.mockMvc.perform(
      get("/api/employees/employment/history")
        .queryParam("zalarisId", "510-00460014")
        .queryParam("ssid", "ssid"));
    verify(findEmploymentHistoryUseCase).find(null, "510-00460014");
  }

}
