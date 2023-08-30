package dev.zalaris.alumni.app.payroll.adapter.data;

import dev.zalaris.alumni.app.infra.dataorigin.DataOrigin;
import dev.zalaris.alumni.app.infra.dataorigin.DataOriginSelector;
import dev.zalaris.alumni.common.domain.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
class FindPayrollResultCompositeDataAccess implements FindPayrollResultDataAccess {

  private final PluginRegistry<FindPayrollResultSingleDataAccess, DataOrigin> findFindPayrollResultSingleDataAccess;
  private final DataOriginSelector dataOriginSelector;

  @Override
  public Optional<Employee> find(String ssid, String zalarisId) {
    var origin = dataOriginSelector.select();
    return findFindPayrollResultSingleDataAccess.getPluginFor(origin)
      .orElseThrow(() -> {
        log.error("No plugin {} found for origin: {}", FindPayrollResultSingleDataAccess.class, origin);
        return new IllegalStateException("Unable to get payroll data");
      })
      .findBySsidAndZalarisId(ssid, zalarisId);
  }
}
