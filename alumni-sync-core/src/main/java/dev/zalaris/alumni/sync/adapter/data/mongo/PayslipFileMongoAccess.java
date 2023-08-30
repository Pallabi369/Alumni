package dev.zalaris.alumni.sync.adapter.data.mongo;

import com.bol.crypt.CryptVault;
import dev.zalaris.alumni.sync.app.payslip.FileMetaData;
import dev.zalaris.alumni.sync.app.payslip.port.outbound.PayslipFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


@RequiredArgsConstructor
@Slf4j
class PayslipFileMongoAccess implements PayslipFileRepository {

  private final GridFsTemplate gridFsTemplate;
  private final CryptVault cryptVault;

  @Override
  public String store(FileMetaData fileMetaData, InputStream file) {
    var name = String.format("payslips-%s-%s.pdf", fileMetaData.getZalarisId(), fileMetaData.getFileIdentifier());
    var objectId = gridFsTemplate.store(encrypt(file), name, MediaType.APPLICATION_PDF_VALUE, fileMetaData);
    return objectId.toString();
  }

  private ByteArrayInputStream encrypt(InputStream inputStream) {
    try {
      var encryptedStream = cryptVault.encrypt(inputStream.readAllBytes());
      return new ByteArrayInputStream(encryptedStream);
    } catch (IOException exception) {
      log.error("Encryption failed with exception", exception);
      throw new RuntimeException(exception);
    }
  }
}
