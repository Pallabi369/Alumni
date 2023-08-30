package dev.zalaris.alumni.app.token.usecase;

public interface UpdateUserDataUseCase {

  void updateUserNationalId(String userId, String encryptedNationalId);
}
