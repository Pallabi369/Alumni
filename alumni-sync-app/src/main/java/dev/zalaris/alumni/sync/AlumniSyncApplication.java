package dev.zalaris.alumni.sync;

import dev.zalaris.alumni.sync.config.CompositeAutoConfigurationImportSelector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import java.util.Locale;
import java.util.TimeZone;

@SpringBootApplication
@EnableAsync
@Import(CompositeAutoConfigurationImportSelector.class)
public class AlumniSyncApplication {

  public static void main(String[] args) {
    SpringApplication.run(AlumniSyncApplication.class, args);
  }

  @PostConstruct
  void started() {
    Locale.setDefault(Locale.ENGLISH);
    TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
  }

}
