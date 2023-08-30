package dev.zalaris.alumni.common.sap;

import dev.zalaris.alumni.common.domain.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.Map;
import java.util.function.Consumer;

import static dev.zalaris.alumni.common.sap.FluxToInputStreamConverter.toInputStream;

@Component
@RequiredArgsConstructor
@Slf4j
@EnableConfigurationProperties(SapApiConfigurationProperties.class)
class FetchPayslipFileService implements FetchPayslipFile {

  private final WebClient webClient;
  private final SapApiConfigurationProperties properties;

  @Override
  public InputStream fetch(String zalarisId, String fileIdentifier) {
    Endpoint endpoint = properties.getEndpoint().getPayslipData();
    return toInputStream(webClient.get()
      .uri(endpoint.getUrl(),
        uri -> uri.build(Map.of(Employee.ZALARIS_ID, zalarisId,
              "fileIdentifier", fileIdentifier,
              "subscription-key", properties.getSubscriptionKey()))
      )
      .accept(MediaType.APPLICATION_PDF, MediaType.APPLICATION_OCTET_STREAM)
      .retrieve()
      .onStatus(status -> status == HttpStatus.NOT_FOUND,
        status -> Mono.error(() -> new PayslipDocumentNotFoundException(zalarisId, fileIdentifier)))
      .bodyToFlux(DataBuffer.class)
      .doOnError(new OnErrorBehaviour(zalarisId, fileIdentifier)));
  }

  private record OnErrorBehaviour(String zalarisId, String fileIdentifier) implements Consumer<Throwable> {
    @Override
    public void accept(Throwable throwable) {
      if (throwable instanceof WebClientResponseException.NotFound) {
        log.debug("Payslip data not found [zalarisId:{}, fileIdentifier:{}])", zalarisId, fileIdentifier, throwable);
      } else {
        var content = throwable instanceof WebClientResponseException ?
          ((WebClientResponseException)throwable).getResponseBodyAsString() : "";
        log.error("Payslip data retrieval failed [zalarisId:{}, fileIdentifier:{}], content = [{}]",
          zalarisId, fileIdentifier, content, throwable);
      }
    }
  }

}
