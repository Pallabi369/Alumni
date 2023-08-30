package dev.zalaris.alumni.app.token.usecase;

import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.core.IBaseClient;
import com.microsoft.graph.http.IHttpProvider;
import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.UserRequestBuilder;
import dev.zalaris.alumni.app.token.TokenProviderProperties;
import okhttp3.Request;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

public class UpdateUserDataUseCaseTest {

  TokenProviderProperties properties;

  GraphServiceClient<Request> graphServiceClient;
  IBaseClient graphClient;
  IHttpProvider httpClient;

  UpdateUserDataB2cInteractor useCase;

  @BeforeEach
  public void init() {
    properties = tokenProviderProperties();
    graphServiceClient = Mockito.mock(GraphServiceClient.class);
    graphClient = Mockito.mock(IBaseClient.class);
    httpClient = Mockito.mock(IHttpProvider.class);
    useCase = new UpdateUserDataB2cInteractor(properties, graphServiceClient);
  }

  private TokenProviderProperties tokenProviderProperties() {
    var b2c = new TokenProviderProperties.B2C();
    b2c.setClientId("B2C_CLIENT_ID");
    var signicat = new TokenProviderProperties.Signicat();
    signicat.setClientId("SIGNICAT_CLIENT_ID");
    signicat.setClientSecret("SIGNICAT_CLIENT_SECRET");
    signicat.setTokenUri("/signicat/token");
    signicat.setRedirectUri("/signicat/redirect");
    signicat.setUserInfoUri("/signicat/userinfo");

    var props = new TokenProviderProperties();
    props.setB2c(b2c);
    props.setSignicat(signicat);
    return props;
  }

  @Test
  public void updateUserNationalId_ok() {
    var userId = "123";
    when(graphServiceClient.users(userId)).thenReturn(new UserRequestBuilder("/b2c", graphClient, null));
    when(graphClient.getHttpProvider()).thenReturn(httpClient);
    when(httpClient.send(any(), any(), any())).thenReturn( new User());
    useCase.updateProperty(userId, "ext_ssid", "123456");

    verify(graphClient).getHttpProvider();
    verify(graphServiceClient).users(userId);
    verify(httpClient).send(any(), any(), any());

  }


  @Test
  public void updateUserNationalId_GraphException() {
    var userId = "123";
    when(graphServiceClient.users(userId)).thenReturn(new UserRequestBuilder("/b2c", graphClient, null));
    when(graphClient.getHttpProvider()).thenReturn(httpClient);
    when(httpClient.send(any(), any(), any())).thenThrow( new ClientException("fake error", null));

    Assertions.assertThrows(IdentityVerificationException.class, () -> {
      useCase.updateProperty(userId, "ext_ssid", "123456");
    });

    verify(graphClient).getHttpProvider();
    verify(graphServiceClient).users(userId);

  }

}
