package dev.zalaris.alumni.app.employee.adapter.mapper;

import com.zalaris.api.nonsap.*;
import dev.zalaris.alumni.app.employee.domain.PersonalDataModel;
import dev.zalaris.alumni.common.domain.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;


@Mapper
public interface PersonalDataMapper extends OrganizationalAssignmentMapper, PersonalInformationMapper, AddressesMapper, CommunicationMapper, BankDetailsMapper, BasicPayMapper, DeductionsMapper {

  @Mapping(source = "employee.ssid", target = "ssid")
  @Mapping(source = "employee.zalarisId", target = "zalarisId")
  @Mapping(source = "employee.p0001", target = "orgAssignment")
  @Mapping(source = "employee.p0002", target = "personalInformation")
  @Mapping(source = "employee.p0006", target = "addresses")
  @Mapping(source = "employee.p0105", target = "communications")
  @Mapping(source = "employee.p0008", target = "basicPay")
  @Mapping(source = "employee.p0009", target = "bankDetails")
  @Mapping(source = "employee.p0014", target = "recurringPaymentDeductions")
  @Mapping(source = "employee.p0015", target = "additionalPaymentDeductions")
  PersonalDataModel mapToPersonalDataModel(Employee employee);


  /**
   * Organizational Assignment mapping (List -> single) based on last sequence number
   */

  default PersonalDataModel.OrganizationalAssignmentGroups mapListToOrganizationalAssignmentGroups(List<DtP0001> input) {
    if (!CollectionUtils.isEmpty(input)) {
      return input.stream()
        .sorted( Comparator.comparing(DtP0001::getBegda).reversed() )
        .map( this::mapToOrganizationalAssignmentGroups )
        .findFirst()
        .get();
    }
    return null;
  }

  @Mapping(source = "input", target = "personnel")
  @Mapping(source = "input", target = "enterpriseStructure")
  @Mapping(source = "input", target = "personnelStructure")
  @Mapping(source = "input", target = "organizationalPlan")
  @Mapping(source = "input", target = "administrator")
  PersonalDataModel.OrganizationalAssignmentGroups mapToOrganizationalAssignmentGroups(DtP0001 input);



  /**
   * Personal information mapping (List -> single) based on last sequence number
   */

  default PersonalDataModel.PersonalInformationGroups mapListToPersonalInformationGroups(List<DtP0002> input) {
    if (!CollectionUtils.isEmpty(input)) {
      return input.stream()
        .sorted( Comparator.comparing(DtP0002::getBegda).reversed() )
        .map( this::mapToPersonalInformationGroups )
        .findFirst()
        .get();
    }
    return null;
  }

  @Mapping(source = "input", target = "personnel")
  @Mapping(source = "input", target = "name")
  @Mapping(source = "input", target = "additionalData")
  PersonalDataModel.PersonalInformationGroups mapToPersonalInformationGroups(DtP0002 input);


  default PersonalDataModel.BasicPayGroups mapListToBasicPayGroups(List<DtP0008> input) {
    if (!CollectionUtils.isEmpty(input)) {
      return input.stream()
        .sorted( Comparator.comparing(DtP0008::getBegda).reversed() )
        .map( this::mapToBasicPayGroups )
        .findFirst()
        .get();
    }
    return null;
  }


  @Mapping(source = "input", target = "personnel")
  @Mapping(source = "input", target = "payscale")
  @Mapping(source = "input", target = "payWages")
  PersonalDataModel.BasicPayGroups mapToBasicPayGroups(DtP0008 input);

}
