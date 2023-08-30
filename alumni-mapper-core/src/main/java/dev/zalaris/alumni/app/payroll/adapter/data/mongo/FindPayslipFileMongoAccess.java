package dev.zalaris.alumni.app.payroll.adapter.data.mongo;

import com.bol.crypt.CryptVault;
import dev.zalaris.alumni.app.infra.dataorigin.DataOrigin;
import dev.zalaris.alumni.app.payroll.adapter.data.FindPayslipFileSingleDataAccess;
import dev.zalaris.alumni.app.payroll.domain.PayslipFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Component
@Profile("mongo")
@RequiredArgsConstructor
@Slf4j
public class FindPayslipFileMongoAccess implements FindPayslipFileSingleDataAccess {

  public static final String FILENAME_PATTERN = "payslips-%s-%s.pdf";

  private final GridFsTemplate gridFsTemplate;
  private final CryptVault cryptVault;

  @Override
  public Optional<PayslipFile> getFile(String ssid, String zalarisId, String sequenceId) {
    var name = String.format(FILENAME_PATTERN, zalarisId, sequenceId);
    var crt = Criteria.where("filename").is(name);
    if (ssid != null) {
      crt.and("metadata.ssid").is(ssid);
    }

    var result = gridFsTemplate.find(Query.query(crt)
      .with(Sort.by(Sort.Direction.DESC, "uploadDate")));

    var resources = StreamSupport.stream(result.spliterator(), false)
      .map(gridFsTemplate::getResource)
      .toList();

    var resource = selectFirst(name, resources);
    return resource
      .map(res -> Pair.of(getSsid(res), toInputStream(res)))
      .map(pair -> new PayslipFile(pair.getLeft(), zalarisId, pair.getRight()));
  }

  private Optional<GridFsResource> selectFirst(String name, List<GridFsResource> resources) {
    if (resources.size() >= 1) {
      if (resources.size() > 1) {
        log.warn("There are {} files with the same name '{}'. Selecting the latest", resources.size(), name);
      }
      return Optional.ofNullable(resources.get(0));
    }
    return Optional.empty();
  }

  private InputStream toInputStream(GridFsResource gridFsResource) {
    try {
      return decrypt(gridFsResource.getInputStream());
    } catch (IOException exception) {
      log.error("Problem loading payslip file, returning empty stream", exception);
      return null;
    }
  }

  private String getSsid(GridFsResource gridFsResource) {
    var fsFile = gridFsResource.getGridFSFile();
    if (fsFile != null) {
      var metadata = fsFile.getMetadata();
      if (metadata != null) {
        return (String) metadata.get("ssid");
      }
    }
    return null;
  }

  private InputStream decrypt(InputStream inputStream) throws IOException {
    var decodedStream = cryptVault.decrypt(inputStream.readAllBytes());
    return new ByteArrayInputStream(decodedStream);
  }

  @Override
  public boolean supports(DataOrigin dataOrigin) {
    return DataOrigin.ALUMNI.equals(dataOrigin);
  }

}
