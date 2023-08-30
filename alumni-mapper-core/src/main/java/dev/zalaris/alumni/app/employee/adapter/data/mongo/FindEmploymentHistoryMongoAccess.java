package dev.zalaris.alumni.app.employee.adapter.data.mongo;

import dev.zalaris.alumni.app.infra.dataorigin.DataOrigin;
import dev.zalaris.alumni.app.employee.adapter.data.FindEmploymentHistorySingleDataAccess;
import dev.zalaris.alumni.common.domain.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("mongo")
@RequiredArgsConstructor
@Slf4j
class FindEmploymentHistoryMongoAccess implements FindEmploymentHistorySingleDataAccess {

  private final MongoTemplate mongoTemplate;

  private static final String ZALARIS_ID_FIELD = "_id";
  private static final String ZALARIS_SSID_FIELD = "ssid";
  private static final String COMPANY_CODE_FIELD = Employee.COMPANY_CODE_FIELD;
  private static final String COMPANY_NAME_FIELD = Employee.COMPANY_NAME_FIELD;
  private static final String START_DATE_FIELD = Employee.START_DATE_FIELD;
  private static final String END_DATE_FIELD = Employee.END_DATE_FIELD;

  @Override
  public List<Employee> findBySsid(String ssid) {
    return find(new Query(Criteria.where("ssid").is(ssid)));
  }

  private List<Employee> find(Query query) {
    query.with(Sort.by(Sort.Direction.ASC, END_DATE_FIELD));
    query.fields().include(
      ZALARIS_ID_FIELD, ZALARIS_SSID_FIELD, COMPANY_CODE_FIELD, COMPANY_NAME_FIELD, START_DATE_FIELD, END_DATE_FIELD);
    log.debug("Executed query against alumni db {}", query);
    return mongoTemplate.find(query, Employee.class, Employee.COLLECTION);
  }

  @Override
  public List<Employee> findByZalarisId(String zalarisId) {
    return find(new Query(Criteria.where(ZALARIS_ID_FIELD).is(zalarisId)));
  }

  @Override
  public boolean supports(DataOrigin delimiter) {
    return DataOrigin.ALUMNI.equals(delimiter);
  }
}
