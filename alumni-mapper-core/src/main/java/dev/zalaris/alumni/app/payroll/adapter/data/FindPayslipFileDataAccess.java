package dev.zalaris.alumni.app.payroll.adapter.data;

import dev.zalaris.alumni.app.payroll.domain.PayslipFile;
import org.springframework.lang.Nullable;

import java.util.Optional;

public interface FindPayslipFileDataAccess {

  Optional<PayslipFile> getFile(@Nullable String ssid, String zalarisId, String sequenceId);

}
