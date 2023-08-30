package dev.zalaris.alumni.app.employee.adapter.mapper;

import com.zalaris.api.nonsap.DtP0001;
import dev.zalaris.alumni.common.domain.Employee;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeMapperTests {


  private DtP0001 of(String seq, String code, String name, String begda, String endda) {
    var p0001 = new DtP0001();
    p0001.setSeqnr(seq);
    p0001.setBukrs(code);
    p0001.setBukrsText(name);
    p0001.setBegda(begda);
    p0001.setEndda(endda);
    return  p0001;
  }

  @Test
  void when0001IsSingle_expectOne() {
    var p0001 = of("1", "2000", "ZapCorp ASA", "20190601", "20220328");
    var employee = new Employee("12345", "510-000", LocalDateTime.now());
    employee.setP0001(Arrays.asList(p0001));

    var service = new EmployeeMapperImpl();
    var result = service.mapToEmployment(employee);
    assertNotNull(result);
    assertEquals(p0001.getBukrs(), result.getCompanyCode());
    assertEquals(p0001.getBukrsText(), result.getCompanyName());
    assertEquals(p0001.getBegda(), result.getStartDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    assertEquals(p0001.getEndda(), result.getEndDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
  }

  @Test
  void when0001IsMultiple_expectCombined() {
    var p0001 = of("2", "2000", "ZapCorp ASA", "20190601", "20220328");
    var r0001 = of("1", "2000", "ZapCorp ASA", "20180101", "20190531");
    var employee = new Employee("12345", "510-000", LocalDateTime.now());
    employee.setP0001(Arrays.asList(p0001, r0001));

    var service = new EmployeeMapperImpl();
    var result = service.mapToEmployment(employee);
    assertNotNull(result);
    assertEquals(p0001.getBukrs(), result.getCompanyCode());
    assertEquals(p0001.getBukrsText(), result.getCompanyName());
    assertEquals(r0001.getBegda(), result.getStartDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    assertEquals(p0001.getEndda(), result.getEndDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
  }

}
