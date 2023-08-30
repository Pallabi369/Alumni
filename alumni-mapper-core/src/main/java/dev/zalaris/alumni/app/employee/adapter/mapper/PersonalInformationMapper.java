package dev.zalaris.alumni.app.employee.adapter.mapper;

import com.zalaris.api.nonsap.DtP0002;
import dev.zalaris.alumni.app.employee.domain.AdditionalData;
import dev.zalaris.alumni.app.employee.domain.Name;
import dev.zalaris.alumni.app.employee.domain.Personnel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PersonalInformationMapper {

  @Mapping(source = "pernr", target = "personnelNo")
  @Mapping(source = "endda", target = "endDate", dateFormat = "yyyyMMdd")
  @Mapping(source = "begda", target = "startDate", dateFormat = "yyyyMMdd")
  @Mapping(source = "aedtm", target = "changedAt")
  Personnel mapToPersonnel(DtP0002 input);

  @Mapping(source = "vorna", target = "firstName")
  @Mapping(source = "nachn", target = "lastName")
  @Mapping(source = "midnm", target = "middleName")
  @Mapping(source = "rufnm", target = "nickName")
  @Mapping(source = "gesch", target = "genderKey")
  @Mapping(source = "inits", target = "initials")
  @Mapping(source = "anred", target = "formOfAddressKey")
  Name mapToName(DtP0002 input);

  @Mapping(source = "perid", target = "personalIdentifier")
  @Mapping(source = "gbdat", target = "birthDate", dateFormat = "yyyyMMdd")
  @Mapping(source = "sprslText", target = "language")
  @Mapping(source = "natioText", target = "nationality", defaultExpression = "java(input.getNatio())")
  @Mapping(source = "famdtText", target = "maritalStatusKey")
  @Mapping(source = "anzkd", target = "nrOfChildren")
  AdditionalData mapToAdditionalData(DtP0002 input);

}
