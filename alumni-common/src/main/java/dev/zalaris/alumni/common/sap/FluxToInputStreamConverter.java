package dev.zalaris.alumni.common.sap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

@Slf4j
class FluxToInputStreamConverter {

  static InputStream toInputStream(Flux<DataBuffer> data) {
    PipedOutputStream outputStream = new PipedOutputStream();
    PipedInputStream inputStream = newPipedInputStream(outputStream);
    DataBufferUtils.write(data, outputStream)
      .doOnError(throwable -> {
        log.error("Unable to access the response stream", throwable);
        try (inputStream) {} // close! otherwise inputStream will wait forever in case of error.
        catch (IOException exception) {
          log.error("Stream could not be closed", exception);
        }
      })
      .doFinally(flux -> {
        try(outputStream) {}
        catch (IOException exception) {
          log.error("Stream could not be closed", exception);
        }
      })
      .subscribe(DataBufferUtils.releaseConsumer());
    return inputStream;
  }

  private static PipedInputStream newPipedInputStream(PipedOutputStream pos) {
    try {
      return new PipedInputStream(pos);
    } catch (Exception exception) {
      log.error("unable to create the output stream", exception);
      throw new RuntimeException();
    }
  }

}
