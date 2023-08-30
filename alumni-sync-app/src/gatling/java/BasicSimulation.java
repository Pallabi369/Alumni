import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class BasicSimulation extends Simulation {

  HttpProtocolBuilder httpProtocol = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0");

  int counter = 1001;
  Iterator<Map<String, Object>> feeder =
    Stream.generate((Supplier<Map<String, Object>>) () -> {
        String ssid = String.valueOf(counter++);
        return Map.of(
          "zalarisId", ssid,
          "ssid", ssid);
      }
    ).iterator();

  ScenarioBuilder scn = scenario("BasicSimulation")
      .feed(feeder)
        .exec(http("sync")
          .post("/sync")
          .body(StringBody("""
          {
             "zalarisId": "#{zalarisId}",
             "ssid": "#{ssid}"
          }"""))
          .asJson());

  {
    setUp(
      scn.injectClosed(
          incrementConcurrentUsers(2)
            .times(5)
            .eachLevelLasting(5)
            .startingFrom(10)
        )
    ).protocols(httpProtocol);
  }
}
