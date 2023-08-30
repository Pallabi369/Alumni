package dev.zalaris.alumni.app.employee.adapter.mapper;

import com.zalaris.api.nonsap.DtP0008;
import com.zalaris.api.nonsap.DtP0014;
import com.zalaris.api.nonsap.DtP0015;
import dev.zalaris.alumni.app.employee.domain.*;
import org.mapstruct.*;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Mapper
public interface BasicPayMapper {

  @Mapping(source = "pernr", target = "personnelNo")
  @Mapping(source = "endda", target = "endDate", dateFormat = "yyyyMMdd")
  @Mapping(source = "begda", target = "startDate", dateFormat = "yyyyMMdd")
  @Mapping(source = "aedtm", target = "changedAt")
  Personnel mapToPersonnel(DtP0008 input);

  @Mapping(source = "subtyText", target = "subtype")
  @Mapping(source = "preas", target = "reason")
  @Mapping(source = "trfarText", target = "type", defaultExpression = "java(input.getTrfar())")
  @Mapping(source = "trfgbText", target = "area", defaultExpression = "java(input.getTrfgb())")
  @Mapping(source = "trfgr", target = "group")
  @Mapping(source = "bsgrd", target = "capacityUtilizationLevel")
  @Mapping(source = "divgv", target = "workingHoursPerPayrollPeriod")
  @Mapping(source = "ansal", target = "annualSalary")
  @Mapping(source = "ancur", target = "annualSalaryCurrencyKey")
  Payscale mapToPayscale(DtP0008 input);

  default List<Paywage> mapToWages(DtP0008 input) {
    if (input == null) {
      return Collections.emptyList();
    }
    return IntStream.range(1,41).mapToObj(idx -> new Paywage(
        Optional
          .ofNullable(getValue(input, String.format("lga%02dText", idx)))
          .orElseGet(() -> getValue(input, String.format("lga%02d", idx))),
        getValue(input, String.format("bet%02d", idx)),
        StringUtils.hasText(input.getWaersText()) ? input.getWaersText() : input.getWaers())
      )
      .filter(paywage -> StringUtils.hasText(paywage.type()))
      .collect(Collectors.toList());
  }

  /**
   * Util method to retrieve cell value from a flatten-table
   *
   * @param input data source
   * @param fieldName column name
   * @return cell value or null
   */
  default String getValue(DtP0008 input, String fieldName) {
    var field = ReflectionUtils.findField(input.getClass(), fieldName);
    ReflectionUtils.makeAccessible(field);
    var value = ReflectionUtils.getField(field, input);
    return value != null ? value.toString() : null;
  }

}
