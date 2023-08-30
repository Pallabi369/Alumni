package dev.zalaris.alumni.common.sap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zalaris.api.nonsap.DTPAMasterdataResponse;
import dev.zalaris.alumni.common.domain.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@Component
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(SapApiConfigurationProperties.class)
class FetchPersonalDataContainerService implements FetchPersonalDataContainer {

  private final WebClient webClient;
  private final SapApiConfigurationProperties properties;
  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public Optional<DTPAMasterdataResponse> fetch(String zalarisId) {
    Endpoint endpoint = properties.getEndpoint().getPersonalData();

    var uri = UriComponentsBuilder.fromUriString(endpoint.getUrl())
      .uriVariables(Map.of(Employee.ZALARIS_ID, zalarisId,
        "subscription-key", properties.getSubscriptionKey(),
        "infotype", "0001,0002,0006,0008,0009,0014,0015,0105",
        "keydate", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
      .build().toUri();

    return Optional.ofNullable(webClient.get()
      .uri(uri)
      .accept(MediaType.APPLICATION_JSON)
      .retrieve()
      .bodyToMono(DTPAMasterdataResponse.class)
      .doOnError(new OnErrorBehaviour(zalarisId))
      .onErrorResume(WebClientResponseException.class,
        ex -> switch (ex.getRawStatusCode()) {
          case 404 -> Mono.empty();
          case 500 -> parseErrorMessage(ex);
          default -> Mono.error(ex);
        })
      .elapsed()
      .doOnNext(elapsedTuple -> log.info("Employee personal data retrieval took {} ms [zalarisId:{}]",
        elapsedTuple.getT1(), zalarisId))
      .map(Tuple2::getT2)
      .block(endpoint.getTimeout()));
  }

  private record OnErrorBehaviour(String zalarisId) implements Consumer<Throwable> {
    @Override
    public void accept(Throwable throwable) {
      if (throwable instanceof WebClientResponseException.NotFound) {
        log.debug("Employee personal data not found [zalarisId:{}])", zalarisId, throwable);
      } else {
        var content = throwable instanceof WebClientResponseException ?
          ((WebClientResponseException)throwable).getResponseBodyAsString() : "";
        log.error("Employee personal data retrieval failed [zalarisId:{}], content = [{}]",
          zalarisId, content, throwable);
      }
    }
  }

  private Mono<? extends DTPAMasterdataResponse> parseErrorMessage(WebClientResponseException exception) {
    try {
      var node = mapper.readTree(exception.getResponseBodyAsString());
      var details = node.path("standard").path("faultDetail");
      var it = details.isArray() ? details.iterator() : List.of(details).iterator();
      while(it.hasNext()) {
        if ("e004".equals(it.next().path("id").asText())) {
          return Mono.empty();
        }
      }
      return Mono.error(exception);
    } catch (JsonProcessingException e) {
      log.debug("Couldn't parse the response body", e);
    }
    return Mono.error(exception);
  }

}
