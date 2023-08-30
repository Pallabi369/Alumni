package dev.zalaris.alumni.app.employee.adapter.data.mongo;

import dev.zalaris.alumni.app.infra.dataorigin.DataOrigin;
import dev.zalaris.alumni.app.employee.adapter.data.FindEmployeeSingleDataAccess;
import dev.zalaris.alumni.common.domain.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Profile("mongo")
@Slf4j
class FindEmployeeMongoAccess implements FindEmployeeSingleDataAccess {

  private final MongoTemplate mongoTemplate;

  @Override
  public Optional<Employee> findBySsidAndZalarisId(String ssid, String zalarisId) {
    var crt = Criteria.where("_id").is(zalarisId);
    if (ssid != null) {
      crt.and("ssid").is(ssid);
    }
    var query = new Query(crt);
    log.debug("Executed query against alumni db {}", query);
    return Optional.ofNullable(mongoTemplate.findOne(query, Employee.class));
  }

  @Override
  public boolean supports(DataOrigin dataOrigin) {
    return DataOrigin.ALUMNI.equals(dataOrigin);
  }

}
