package dev.zalaris.alumni.sync.app.payslip;

import dev.zalaris.alumni.sync.app.payslip.port.outbound.PayslipFileProvider;
import dev.zalaris.alumni.sync.app.payslip.port.outbound.PayslipFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
class PayslipFileJobLauncher {

  private final PayslipFileProvider provider;
  private final PayslipFileRepository repository;

  public boolean launch(String jobName, PayslipFileMessage message) {
    log.info("{} has started [{}]", jobName, message);
    try {
      var file = provider.fetchFileInputStream(message.zalarisId(), message.fileIdentifier());

      var metaData = new FileMetaData(message.ssid(), message.zalarisId(), message.fileIdentifier());
      log.info("Saving payslip file with identifier {} for employee {}",
        metaData.getFileIdentifier(), metaData.getZalarisId());
      String fileId = repository.store(metaData, file);
      log.debug("File stored under key {} ", fileId);

      return true;
    } catch (Exception exception) {
      log.error("An exception occurred during payslip-file processing", exception);
      return false;
    }
  }

}
