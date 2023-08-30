package dev.zalaris.alumni.app.employee.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Personnel {
  private String personnelNo;
  private String changedAt;
  private LocalDate startDate;
  private LocalDate endDate;
}
