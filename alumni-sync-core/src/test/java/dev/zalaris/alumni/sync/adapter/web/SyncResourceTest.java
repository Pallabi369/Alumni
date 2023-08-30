package dev.zalaris.alumni.sync.adapter.web;

import dev.zalaris.alumni.sync.app.employee.EmployeeMessageDeliveryException;
import dev.zalaris.alumni.sync.app.employee.port.inbound.CreateEmployeeUseCase;
import dev.zalaris.alumni.sync.app.employee.port.inbound.UpdateExpirationDateUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@WebMvcTest
@ContextConfiguration(classes = {WebTestConfig.class})
@Import(SyncResource.class)
public class SyncResourceTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private CreateEmployeeUseCase createEmployeeUseCase;

  @MockBean
  private UpdateExpirationDateUseCase expirationDateUseCase;

  @BeforeEach
  public void init() {
    Mockito.reset(createEmployeeUseCase, expirationDateUseCase);
  }

  @Test
  public void whenExpDateIsMissing_400() throws Exception {
    mvc.perform(MockMvcRequestBuilders.post("/sync")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content("""
          {
              "ssid": "123456",
              "zalarisId": "510-00000000"
          }
          """))
      .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  public void whenExpDateIsEmpty_400() throws Exception {
    mvc.perform(MockMvcRequestBuilders.post("/sync")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content("""
          {
              "ssid": "123456",
              "zalarisId": "510-00000000",
              "expirationDate": ""
          }
          """))
      .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  public void whenExpDateIsInPast_400() throws Exception {
    mvc.perform(MockMvcRequestBuilders.post("/sync")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content("""
          {
              "ssid": "123456",
              "zalarisId": "510-00000000",
              "expirationDate": "%s"
          }
          """.formatted(pastDate())))
      .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  public void whenZalarisIdIsNotInPattern_400() throws Exception {
    mvc.perform(MockMvcRequestBuilders.post("/sync")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content("""
          {
              "ssid": "123456",
              "zalarisId": "510-123",
              "expirationDate": "%s"
          }
          """.formatted(futureDate())))
      .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  public void whenMsgNotDelivered_502() throws Exception {
    var zalarisId = "510-00000000";
    Mockito
      .doThrow(new EmployeeMessageDeliveryException(zalarisId))
      .when(createEmployeeUseCase).create(anyString(), anyString(), any());

    mvc.perform(MockMvcRequestBuilders.post("/sync")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content("""
          {
              "ssid": "123456",
              "zalarisId": "510-00000000",
              "expirationDate": "%s"
          }
          """.formatted(futureDate())))
      .andExpect(MockMvcResultMatchers.status().isBadGateway());

    Mockito.verify(createEmployeeUseCase).create(anyString(), anyString(), any());
  }

  @Test
  public void whenExpDateIsCorrect_201() throws Exception {
    mvc.perform(MockMvcRequestBuilders.post("/sync")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content("""
          {
              "ssid": "123456",
              "zalarisId": "510-00000000",
              "expirationDate": "%s"
          }
          """.formatted(futureDate())))
      .andExpect(MockMvcResultMatchers.status().isCreated());

    Mockito.verify(createEmployeeUseCase).create(anyString(), anyString(), any());
  }

  private String futureDate() {
    var date = LocalDateTime.now().plus(1, ChronoUnit.YEARS);
    return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
  }

  private String pastDate() {
    var date = LocalDateTime.now().minus(1, ChronoUnit.YEARS);
    return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
  }

  @Test
  public void updateExpirationTime_200() throws Exception {
    mvc.perform(MockMvcRequestBuilders.put("/expirationDate")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content("""
          {
              "zalarisId": "510-00000000",
              "expirationDate": "%s"
          }
          """.formatted(futureDate())))
      .andExpect(MockMvcResultMatchers.status().isOk());

    Mockito.verify(expirationDateUseCase).updateDateExpiration(anyString(), any());
  }

}
