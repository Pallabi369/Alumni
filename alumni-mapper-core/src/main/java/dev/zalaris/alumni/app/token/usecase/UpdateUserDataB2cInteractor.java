package dev.zalaris.alumni.app.token.usecase;

import com.google.gson.JsonPrimitive;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.GraphServiceClient;
import dev.zalaris.alumni.app.token.TokenProviderProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;

@RequiredArgsConstructor
@Slf4j
public class UpdateUserDataB2cInteractor implements UpdateUserDataUseCase {

  private final TokenProviderProperties properties;
  private final GraphServiceClient<Request> graphServiceClient;

  public void updateUserNationalId(String userId, String encryptedNationalId) {
    var b2c = properties.getB2c();
    updateProperty(userId, b2c.getSsidProperty(), encryptedNationalId);
  }

  void updateProperty(String userId, String property, String value) {
    User user = new User();
    user.additionalDataManager().put(property, new JsonPrimitive(value));

    try {

      graphServiceClient.users(userId).buildRequest().patch(user);
      log.info("User {} ssid property {} has been set to nationalId", user, property);

    } catch (ClientException e) {
      var msg = "Error updating user data in b2c";
      log.error(String.format("%s [user:%s]", msg, userId), e);
      throw new IdentityVerificationException(msg, e);
    }
  }




}
