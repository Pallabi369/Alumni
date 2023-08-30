package dev.zalaris.alumni.app.employee.adapter.mapper;

import com.zalaris.api.nonsap.DtP0001;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PersonalDataMapperTests {

  private DtP0001 of(String begda, String job) {
    var p0001 = new DtP0001();
    p0001.setStellText(job);
    p0001.setBegda(begda);
    return  p0001;
  }

  @Test
  void when0001IsSingle_expectOne() {
    var p_1_0001 = of("20201010", "Janitor");
    var p_2_0001 = of("20201011", "CFO");

    var service = new PersonalDataMapperImpl();
    var result = service.mapListToOrganizationalAssignmentGroups(Arrays.asList(p_1_0001, p_2_0001));
    assertNotNull(result);
    assertEquals(p_2_0001.getStellText(), result.organizationalPlan().getJob());

  }
}
