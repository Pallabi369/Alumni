package dev.zalaris.alumni.common.sap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zalaris.api.nonsap.DTPayrollResultsResponse;
import com.zalaris.api.nonsap.PayrollResult;
import dev.zalaris.alumni.common.domain.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Slf4j
@EnableConfigurationProperties(SapApiConfigurationProperties.class)
class FetchPayrollResultsService implements FetchPayrollResults {

  private final WebClient webClient;
  private final SapApiConfigurationProperties properties;
  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public List<PayrollResult> fetch(String zalarisId) {
    Endpoint endpoint = properties.getEndpoint().getPayrollData();
    return webClient.get()
      .uri(endpoint.getUrl(),
        uri -> uri.build(Map.of(Employee.ZALARIS_ID, zalarisId,
              "subscription-key", properties.getSubscriptionKey()))
      )
      .accept(MediaType.APPLICATION_JSON)
      .retrieve()
      .bodyToMono(DTPayrollResultsResponse.class)
      .doOnError(new OnErrorBehaviour(zalarisId))
      .onErrorResume(WebClientResponseException.class,
        ex -> switch (ex.getRawStatusCode()) {
          case 404 -> Mono.justOrEmpty(new DTPayrollResultsResponse());
          case 500 -> parseErrorMessage(ex);
          default -> Mono.error(ex);
        })
      .elapsed()
      .doOnNext(elapsedTuple -> log.info("Employee payroll data retrieval took {} ms [zalarisId:{}]",
        elapsedTuple.getT1(), zalarisId))
      .map(Tuple2::getT2)
      .map(DTPayrollResultsResponse::getPayrollResults)
      .block(endpoint.getTimeout());
  }

  private record OnErrorBehaviour(String zalarisId) implements Consumer<Throwable> {
    @Override
    public void accept(Throwable throwable) {
      if (throwable instanceof WebClientResponseException.NotFound) {
        log.debug("Employee payroll data not found [zalarisId:{}])", zalarisId, throwable);
      } else {
        var content = throwable instanceof WebClientResponseException ?
          ((WebClientResponseException)throwable).getResponseBodyAsString() : "";
        log.error("Employee payroll data retrieval failed [zalarisId:{}], content = [{}]", zalarisId, content, throwable);
      }
    }
  }

  private Mono<? extends DTPayrollResultsResponse> parseErrorMessage(WebClientResponseException exception) {
    try {
      var node = mapper.readTree(exception.getResponseBodyAsString());
      var details = node.path("standard").path("faultDetail");
      var it = details.isArray() ? details.iterator() : List.of(details).iterator();
      while(it.hasNext()) {
        var result = parseError(it.next().path("id").asText());
        if (result.isPresent()) {
          return Mono.justOrEmpty(result.get());
        }
      }
    } catch (JsonProcessingException e) {
      log.debug("Couldn't parse the response body", e);
    }
    return Mono.error(exception);
  }

  private Optional<DTPayrollResultsResponse> parseError(String errorId) {
    if (StringUtils.hasText(errorId)) {
      return switch (errorId) {
        case "e009", "e004" -> Optional.of(new DTPayrollResultsResponse());
        default -> Optional.empty();
      };
    }
    return Optional.empty();
  }

}
