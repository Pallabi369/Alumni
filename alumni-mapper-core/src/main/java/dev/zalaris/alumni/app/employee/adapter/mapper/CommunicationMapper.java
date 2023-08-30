package dev.zalaris.alumni.app.employee.adapter.mapper;


import com.zalaris.api.nonsap.DtP0105;
import dev.zalaris.alumni.app.employee.domain.Communication;
import dev.zalaris.alumni.app.employee.domain.Personnel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CommunicationMapper {

  @Mapping(source = "pernr", target = "personnelNo")
  @Mapping(source = "endda", target = "endDate", dateFormat = "yyyyMMdd")
  @Mapping(source = "begda", target = "startDate", dateFormat = "yyyyMMdd")
  @Mapping(source = "aedtm", target = "changedAt")
  Personnel mapToPersonnel(DtP0105 input);

  @Mapping(source = "subtyText", target = "type")
  @Mapping(source = "usrid", target = "communicationId", defaultExpression = "java(input.getUsridLong())")
  Communication mapToCommunication(DtP0105 input);

}
