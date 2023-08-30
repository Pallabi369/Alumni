package dev.zalaris.alumni.app.payroll.adapter.data;

import dev.zalaris.alumni.app.infra.dataorigin.DataOrigin;
import dev.zalaris.alumni.app.infra.dataorigin.DataOriginSelector;
import dev.zalaris.alumni.app.payroll.domain.PayslipFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
class FindPayslipFileCompositeDataAccess implements FindPayslipFileDataAccess {

  private final PluginRegistry<FindPayslipFileSingleDataAccess, DataOrigin> findPayslipFileSingleDataAccessRegistry;
  private final DataOriginSelector dataOriginSelector;

  @Override
  public Optional<PayslipFile> getFile(String ssid, String zalarisId, String sequenceId) {
    var origin = dataOriginSelector.select();
    return findPayslipFileSingleDataAccessRegistry.getPluginFor(origin)
      .orElseThrow(() -> {
        log.error("No plugin {} found for origin: {}", FindPayslipFileSingleDataAccess.class, origin);
        return new IllegalStateException("Unable to get payslip data");
      }).getFile(ssid, zalarisId, sequenceId);
  }
}
