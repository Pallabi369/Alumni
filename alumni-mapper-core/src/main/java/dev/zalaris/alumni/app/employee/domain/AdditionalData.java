package dev.zalaris.alumni.app.employee.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AdditionalData {
  private String personalIdentifier;
  private LocalDate birthDate;
  private String language;
  private String nationality;
  private String maritalStatusKey;
  private String nrOfChildren;
}
