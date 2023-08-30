package dev.zalaris.alumni.app.employee.domain;

import lombok.Data;

@Data
public class Name {
  private String formOfAddressKey;
  private String firstName;
  private String lastName;
  private String middleName;
  private String nickName;
  private String genderKey;
  private String initials;
}
