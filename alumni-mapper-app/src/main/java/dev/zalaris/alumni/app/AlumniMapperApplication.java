package dev.zalaris.alumni.app;

import dev.zalaris.alumni.common.encrypt.MongoEncryptConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;
import java.util.Locale;
import java.util.TimeZone;

@SpringBootApplication
@Import(MongoEncryptConfig.class)
public class AlumniMapperApplication {

  public static void main(String[] args) {
    SpringApplication.run(AlumniMapperApplication.class, args);
  }

  @PostConstruct
  void started() {
    Locale.setDefault(Locale.ENGLISH);
    TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
  }

}
