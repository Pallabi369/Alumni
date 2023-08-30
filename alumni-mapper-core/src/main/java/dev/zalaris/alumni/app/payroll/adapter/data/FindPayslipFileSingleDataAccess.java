package dev.zalaris.alumni.app.payroll.adapter.data;

import dev.zalaris.alumni.app.infra.dataorigin.DataOrigin;
import dev.zalaris.alumni.app.payroll.domain.PayslipFile;
import org.springframework.plugin.core.Plugin;

import java.util.Optional;

public interface FindPayslipFileSingleDataAccess extends Plugin<DataOrigin> {

  Optional<PayslipFile> getFile(String ssid, String zalarisId, String sequenceId);

}
