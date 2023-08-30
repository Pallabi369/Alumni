package dev.zalaris.alumni.app.infra.access;

import java.util.Optional;

public interface ClaimAccessor {

  String ZALARIS_ID = "extension_ZalarisID";
  String SSID = "extension_ssid";

  <T> Optional<T> get(String claimName);

}
