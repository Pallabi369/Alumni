package dev.zalaris.alumni.app.token.web;

import dev.zalaris.alumni.app.token.usecase.GetUserSsidUseCase;
import dev.zalaris.alumni.app.token.usecase.IdentityVerificationException;
import dev.zalaris.alumni.app.token.usecase.UpdateUserDataUseCase;
import dev.zalaris.alumni.common.web.error.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = {TokenProviderResource.class, TestSecurityConfiguration.class})
@WebMvcTest(TokenProviderResource.class)
public class TokenProviderResourceTest {


  @MockBean GetUserSsidUseCase getUserSsidUseCase;
  @MockBean UpdateUserDataUseCase updateUserDataUseCase;;

  @Autowired TokenProviderResource tokenProviderResource;
  @Autowired MockMvc mockMvc;

  @Test
  @WithMockUser(username = "user")
  void whenCodeIsProvided_expect200() throws Exception {
    var encSsId = "AblaAblaSsId";
    var code = "123";
    when(getUserSsidUseCase.getSsid(code)).thenReturn(encSsId);
    this.mockMvc.perform(post("/api/tokens?code=" + code)).andExpect(status().is(200));
    verify(getUserSsidUseCase).getSsid(code);
    verify(updateUserDataUseCase).updateUserNationalId(eq("user"), eq(encSsId));
  }

  @Test
  @WithMockUser(username = "user")
  void whenCodeIsMissing_expect400() throws Exception {
    this.mockMvc.perform(post("/api/tokens")).andExpect(status().is(400));
  }

  @Test
  @WithMockUser(username = "user")
  void whenCannotGetAccessToken_expect400() throws Exception {
    var code = "";
    when(getUserSsidUseCase.getSsid(code)).thenThrow(new IdentityVerificationException("Error retrieving access token from given code", new NullPointerException()));
    this.mockMvc.perform(post("/api/tokens?code=" + code))
      .andExpect(status().is(400))
      .andExpect(jsonPath("$.errors[0].code").value(ErrorCode.IDENTITY_VERIFICATION_ERROR));
    verify(getUserSsidUseCase).getSsid(code);
  }

}
