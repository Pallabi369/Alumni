package dev.zalaris.alumni.app.employee.adapter.mapper;

import com.zalaris.api.nonsap.DtP0001;
import dev.zalaris.alumni.app.employee.domain.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface OrganizationalAssignmentMapper {

  @Mapping(source = "pernr", target = "personnelNo")
  @Mapping(source = "endda", target = "endDate", dateFormat = "yyyyMMdd")
  @Mapping(source = "begda", target = "startDate", dateFormat = "yyyyMMdd")
  @Mapping(source = "aedtm", target = "changedAt")
  Personnel mapToPersonnel(DtP0001 input);

  @Mapping(source = "bukrs", target = "companyCode")
  @Mapping(source = "bukrsText", target = "companyName")
  @Mapping(source = "werksText", target = "personnelArea")
  @Mapping(source = "btrtlText", target = "personnelSubarea")
  @Mapping(source = "kostl", target = "costCenter")
  @Mapping(source = "kostlText", target = "costCenterName")
  EnterpriseStructure mapToEnterpriseStructure(DtP0001 input);

  @Mapping(source = "abkrsText", target = "payrollArea")
  @Mapping(source = "ansvhText", target = "contract")
  @Mapping(source = "persgText", target = "employeeGroup")
  @Mapping(source = "perskText", target = "employeeSubgroup")
  PersonnelStructure mapToPersonnelStructure(DtP0001 input);

  @Mapping(source = "plansText", target = "position")
  @Mapping(source = "stellText", target = "job")
  @Mapping(source = "orgehText", target = "organizationUnit")
  @Mapping(source = "vdsk1", target = "organizationKey")
  OrganizationalPlan mapToOrganizationalPlan(DtP0001 input);

  @Mapping(source = "sbmodText", target = "group")
  @Mapping(source = "sachzText", target = "timeRecording")
  @Mapping(source = "sachpText", target = "hr")
  @Mapping(source = "sachaText", target = "payroll")
  @Mapping(source = "mstbrText", target = "supervisor")
  Administrator mapToAdministrator(DtP0001 input);

}
