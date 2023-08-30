package dev.zalaris.alumni.app.employee.domain;

import lombok.Data;

@Data
public class Payscale {

  private String subtype;
  private String reason;
  private String type;
  private String area;
  private String group;

  private String capacityUtilizationLevel;
  private String workingHoursPerPayrollPeriod;
  private String annualSalary;
  private String annualSalaryCurrencyKey;

}
