package dev.zalaris.alumni.app.employee.adapter.mapper;

import com.zalaris.api.nonsap.DtP0006;
import dev.zalaris.alumni.app.employee.domain.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AddressesMapper {

  @Mapping(source="telnr", target = "phoneNumber")
  @Mapping(source="land1Text", target = "country")
  @Mapping(source="state", target = "state")
  @Mapping(source="ort02", target = "district")
  @Mapping(source="ort01", target = "city")
  @Mapping(source="pstlz", target = "postalCode")
  @Mapping(source="posta", target = "apartment")
  @Mapping(source="hsnmr", target = "houseNumber")
  @Mapping(source="stras", target = "street")
  @Mapping(source="locat", target = "street2ndLine")
  @Mapping(source = "subtyText", target = "addressType")
  Address mapToAddress(DtP0006 input);

}
