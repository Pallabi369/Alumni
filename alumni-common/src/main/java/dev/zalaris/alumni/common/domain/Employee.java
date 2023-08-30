package dev.zalaris.alumni.common.domain;

import com.zalaris.api.nonsap.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = Employee.COLLECTION)
@Slf4j
public class Employee {

  public static final String COLLECTION = "employees";
  public static final String SSID = "ssid";
  public static final String ZALARIS_ID = "zalarisId";
  public static final String COMPANY_CODE_FIELD = "p0001.bukrs";
  public static final String COMPANY_NAME_FIELD = "p0001.bukrsText";
  public static final String START_DATE_FIELD = "p0001.begda";
  public static final String END_DATE_FIELD = "p0001.endda";

  /**
   * The unique employee identifier assigned by an external unit (eg. signicat).
   */
  @Indexed
  private String ssid;

  /**
   * The unique employment identifier. It consists of the company prefix and the employee id unique within the company.
   * E.g. 501-xyz123blah
   */
  @Id
  private String zalarisId;

  @Indexed(expireAfterSeconds=60)
  private LocalDateTime expirationDate;

  public Employee(String ssid, String zalarisId, LocalDateTime expirationDate) {
    this.ssid = ssid;
    this.zalarisId = zalarisId;
    this.expirationDate = expirationDate;
  }

  public Employee(String zalarisId, DTPAMasterdataResponse dataContainer) {
    this("__unsupported__", zalarisId, LocalDateTime.MAX);
    setPersonalDataContainer(dataContainer);
  }

  public Employee(String zalarisId, List<PayrollResult> payrollResults) {
    this("__unsupported__", zalarisId, LocalDateTime.MAX);
    setPayrollResults(payrollResults);
  }

  private List<DtP0001> p0001;
  private List<DtP0002> p0002;
  private List<DtP0006> p0006;
  private List<DtP0008> p0008;
  private List<DtP0009> p0009;
  private List<DtP0014> p0014;
  private List<DtP0015> p0015;
  private List<DtP0105> p0105;

  private List<PayrollResult> payrollResults = new ArrayList<>();

  public void setPersonalDataContainer(DTPAMasterdataResponse dataContainer) {
    this.p0001 = dataContainer.getP0001();
    this.p0002 = dataContainer.getP0002();
    this.p0006 = dataContainer.getP0006();
    this.p0008 = dataContainer.getP0008();
    this.p0009 = dataContainer.getP0009();
    this.p0014 = dataContainer.getP0014();
    this.p0015 = dataContainer.getP0015();
    this.p0105 = dataContainer.getP0105();
  }

  public String getCompanyCodeValue() {
    if (p0001 != null && p0001.size() == 1) {
      return p0001.get(0).getBukrs();
    }
    return null;
  }

}
