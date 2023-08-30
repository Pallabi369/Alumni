package dev.zalaris.alumni.common.sap;

import java.io.InputStream;

public interface FetchPayslipFile {

  InputStream fetch(String zalarisId, String fileIdentifier);

}
