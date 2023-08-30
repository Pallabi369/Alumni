package dev.zalaris.alumni.app.employee.adapter.mapper;

import com.zalaris.api.nonsap.DtP0014;
import com.zalaris.api.nonsap.DtP0015;
import dev.zalaris.alumni.app.employee.domain.AdditionalPaymentDeduction;
import dev.zalaris.alumni.app.employee.domain.RecurringPaymentDeduction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DeductionsMapper {

  @Mapping(source = "betrg", target = "amount")
  @Mapping(source = "subtyText", target = "type", defaultExpression = "java(input.getSubty())")

  @Mapping(source = "waers", target = "currency")
  @Mapping(source = "endda", target = "endDate", dateFormat = "yyyyMMdd")
  @Mapping(source = "begda", target = "startDate", dateFormat = "yyyyMMdd")
  RecurringPaymentDeduction mapToRecurringPaymentDeduction(DtP0014 input);

  @Mapping(source = "betrg", target = "amount")
  @Mapping(source = "subtyText", target = "type", defaultExpression = "java(input.getSubty())")
  @Mapping(source = "waers", target = "currency")
  @Mapping(source = "endda", target = "endDate", dateFormat = "yyyyMMdd")
  @Mapping(source = "begda", target = "startDate", dateFormat = "yyyyMMdd")
  AdditionalPaymentDeduction mapToAdditionalPaymentDeduction(DtP0015 input);
}
