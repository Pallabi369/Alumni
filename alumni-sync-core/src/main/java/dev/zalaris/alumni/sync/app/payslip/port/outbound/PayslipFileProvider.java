package dev.zalaris.alumni.sync.app.payslip.port.outbound;

import java.io.InputStream;

public interface PayslipFileProvider {

  InputStream fetchFileInputStream(String zalarisId, String fileIdentifier);

}
