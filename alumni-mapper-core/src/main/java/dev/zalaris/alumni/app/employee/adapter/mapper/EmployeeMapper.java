package dev.zalaris.alumni.app.employee.adapter.mapper;

import com.zalaris.api.nonsap.DtP0001;
import com.zalaris.api.nonsap.DtP0008;
import dev.zalaris.alumni.app.employee.domain.Employment;
import dev.zalaris.alumni.app.employee.domain.EmploymentHistoryModel;
import dev.zalaris.alumni.app.employee.domain.Paywage;
import dev.zalaris.alumni.common.domain.Employee;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface EmployeeMapper {

  default Employment mapToEmployment(Employee employee) {
    var builder = Employment.builder()
      .zalarisId(employee.getZalarisId());

    if (!CollectionUtils.isEmpty(employee.getP0001())) {
      var p0001 = employee.getP0001().stream()
        .sorted(Comparator.comparing(DtP0001::getEndda).reversed())
        .findAny();
        builder.companyCode(p0001.get().getBukrs());
        builder.companyName(p0001.get().getBukrsText());

        var start = employee.getP0001().stream()
          .map(DtP0001::getBegda)
          .min(Comparator.naturalOrder());
        var end = employee.getP0001().stream()
          .map(DtP0001::getEndda)
          .max(Comparator.naturalOrder());

        builder.startDate(LocalDate.parse(start.get(), DateTimeFormatter.ofPattern("yyyyMMdd")));
        builder.endDate(LocalDate.parse(end.get(), DateTimeFormatter.ofPattern("yyyyMMdd")));

    }

    return builder.build();
  }

  @Mapping(target = "ssid")
  @Mapping(source = "employees", target = "employments")
  EmploymentHistoryModel mapToEmploymentHistoryModel(String ssid, List<Employee> employees);

}
