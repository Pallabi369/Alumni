package dev.zalaris.alumni.common.encrypt;

import com.bol.crypt.CryptVault;
import com.bol.secure.CachedEncryptionEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class MongoEncryptConfig {

  @Value("${mongodb.encrypt.fields}")
  private Set<String> fields;

  @Bean
  public CachedEncryptionEventListener encryptionEventListener(CryptVault cryptVault) {
    return new CachedEncryptionEventListener(cryptVault, field -> {
      var fieldKey = field.getDeclaringClass().getSimpleName() + "." + field.getName();
      return fields.contains(fieldKey);
    });
  }

}
