package dev.zalaris.alumni.app.employee.adapter.data;

import dev.zalaris.alumni.app.infra.dataorigin.DataOrigin;
import dev.zalaris.alumni.app.infra.dataorigin.DataOriginSelector;
import dev.zalaris.alumni.common.domain.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
class FindEmployeeCompositeDataAccess implements FindEmployeeDataAccess {

  private final PluginRegistry<FindEmployeeSingleDataAccess, DataOrigin> findEmployeeSingleDataAccessRegistry;
  private final DataOriginSelector dataOriginSelector;

  @Override
  public Optional<Employee> find(String ssid, String zalarisId) {
    log.info("Find employee: [ssid={}, zalarisId={}]", ssid, zalarisId);
    var origin = dataOriginSelector.select();
    return findEmployeeSingleDataAccessRegistry.getPluginFor(origin)
      .orElseThrow(() -> {
        log.error("No plugin {} found for origin: {}", FindEmployeeSingleDataAccess.class, origin);
        return new IllegalStateException("Unable to get employee data");
      })
      .findBySsidAndZalarisId(ssid, zalarisId);
  }

}
