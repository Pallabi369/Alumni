package dev.zalaris.alumni.app.employee.domain;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class Employment {
  String zalarisId;
  String companyCode;
  String companyName;
  LocalDate startDate;
  LocalDate endDate;
}
