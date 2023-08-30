package dev.zalaris.alumni.app.employee.adapter.data;

import dev.zalaris.alumni.app.infra.dataorigin.DataOrigin;
import dev.zalaris.alumni.app.infra.dataorigin.DataOriginSelector;
import dev.zalaris.alumni.common.domain.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
class FindEmploymentHistoryCompositeDataAccess implements FindEmploymentHistoryDataAccess {

  private final PluginRegistry<FindEmploymentHistorySingleDataAccess, DataOrigin> findEmploymentHistorySingleDataAccessRegistry;
  private final DataOriginSelector dataOriginSelector;

  @Override
  public List<Employee> find(String ssid, String zalarisId) {
    log.info("Find employment history: [ssid={}, zalarisId={}]", ssid, zalarisId);
    var origin = dataOriginSelector.select();
    var dataAccess = findEmploymentHistorySingleDataAccessRegistry.getPluginFor(origin)
      .orElseThrow(() -> {
        log.error("No plugin {} found for origin: {}", FindEmploymentHistorySingleDataAccess.class, origin);
        return new IllegalStateException("Unable to get employment history");
      });

    return switch (dataOriginSelector.select()) {
      case SAP_API -> dataAccess.findByZalarisId(zalarisId);
      case ALUMNI -> ssid != null ? dataAccess.findBySsid(ssid) : dataAccess.findByZalarisId(zalarisId);
    };
  }

}
