package dev.zalaris.alumni.sync.adapter.data.mongo;

import com.zalaris.api.nonsap.DTPAMasterdataResponse;
import com.zalaris.api.nonsap.PayrollResult;
import dev.zalaris.alumni.common.domain.Employee;
import dev.zalaris.alumni.sync.app.employee.EmployeeNotFoundException;
import dev.zalaris.alumni.sync.app.employee.port.outbound.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.LocalDateTime;
import java.util.List;

import static dev.zalaris.alumni.common.domain.Employee.ZALARIS_ID;

@RequiredArgsConstructor
@Slf4j
class EmployeeMongoAccess implements EmployeeRepository {

  private final MongoOperations mongoOperations;

  @Override
  public boolean create(Employee employee) {
    Document sink = new Document();
    mongoOperations.getConverter().write(employee, sink);
    var save = Update.fromDocument(sink);
    var update = new Update();
    save.getUpdateObject().forEach(update::setOnInsert);

    var query = Query.query(Criteria.where(ZALARIS_ID).is(employee.getZalarisId()));
    var result = mongoOperations.upsert(query, update, Employee.class);
    return result.getUpsertedId() != null;
  }

  @Override
  public void update(String zalarisId, DTPAMasterdataResponse personalDataContainer) {
    Query query = new Query(new Criteria(ZALARIS_ID).is(zalarisId));
    var employee = mongoOperations.findOne(query, Employee.class);
    if (employee == null) {
      throw new EmployeeNotFoundException("""
        No such employee found: %s""".formatted(zalarisId));
    }
    employee.setPersonalDataContainer(personalDataContainer);
    mongoOperations.save(employee);
  }

  @Override
  public void update(String zalarisId, List<PayrollResult> payrollResultsList) {
    Query query = new Query(new Criteria(ZALARIS_ID).is(zalarisId));
    var employee = mongoOperations.findOne(query, Employee.class);
    if (employee == null) {
      throw new EmployeeNotFoundException("""
        No such employee found: %s""".formatted(zalarisId));
    }
    employee.setPayrollResults(payrollResultsList);
    mongoOperations.save(employee);
  }

  @Override
  public void update(String zalarisId, LocalDateTime expirationDate) {
    Query query = new Query(new Criteria(ZALARIS_ID).is(zalarisId));
    var employee = mongoOperations.findOne(query, Employee.class);
    if (employee == null) {
      throw new EmployeeNotFoundException("""
        No such employee found: %s""".formatted(zalarisId));
    }
    employee.setExpirationDate(expirationDate);
    mongoOperations.save(employee);
  }


}
