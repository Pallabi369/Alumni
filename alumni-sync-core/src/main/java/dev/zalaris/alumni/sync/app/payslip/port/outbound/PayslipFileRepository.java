package dev.zalaris.alumni.sync.app.payslip.port.outbound;

import dev.zalaris.alumni.sync.app.payslip.FileMetaData;

import java.io.InputStream;

public interface PayslipFileRepository {

  String store(FileMetaData fileMetaData, InputStream file);
}
