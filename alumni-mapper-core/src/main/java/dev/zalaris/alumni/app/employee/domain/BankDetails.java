package dev.zalaris.alumni.app.employee.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BankDetails {

  private String bankDetailsType;
  private String payee;
  private String postCode;
  private String city;
  private String bankCountryKey;
  private String bankKey;
  private String bankNumber;
  private String bankControlKey;
  private String swift;
  private String paymentMethod;
  private String currency;
  private LocalDate startDate;
  private LocalDate endDate;

}
