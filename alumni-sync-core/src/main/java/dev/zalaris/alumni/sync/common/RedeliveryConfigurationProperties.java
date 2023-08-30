package dev.zalaris.alumni.sync.common;

import dev.zalaris.sync.Redelivery;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class RedeliveryConfigurationProperties {

  private int initial = 10000;
  private int multiplier = 2;
  private int maxAttempt = 5;
  private int maxDelay = 900000;

  public Redelivery build(String name) {
    var redelivery = new Redelivery(initial, multiplier, maxAttempt, maxDelay);
    log.info("{} redelivery configuration: {}", name, redelivery);
    return redelivery;
  }

}
