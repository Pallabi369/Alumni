package dev.zalaris.alumni.app.employee.adapter.mapper;

import com.zalaris.api.nonsap.DtP0009;
import dev.zalaris.alumni.app.employee.domain.BankDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BankDetailsMapper {

  @Mapping(source = "bnksaText", target = "bankDetailsType", defaultExpression = "java(input.getBnksa())")
  @Mapping(source = "emftx", target = "payee")
  @Mapping(source = "bkplz", target = "postCode")
  @Mapping(source = "bkort", target = "city")
  @Mapping(source = "banksText", target = "bankCountryKey", defaultExpression = "java(input.getBanks())")
  @Mapping(source = "bankl", target = "bankKey")
  @Mapping(source = "bankn", target = "bankNumber")
  @Mapping(source = "bankp", target = "bankControlKey")
  @Mapping(source = "swift", target = "swift")
  @Mapping(source = "zlschText", target = "paymentMethod", defaultExpression = "java(input.getZlsch())")
  @Mapping(source = "waers", target = "currency")
  @Mapping(source = "endda", target = "endDate", dateFormat = "yyyyMMdd")
  @Mapping(source = "begda", target = "startDate", dateFormat = "yyyyMMdd")
  BankDetails mapToBankDetails(DtP0009 input);


}
