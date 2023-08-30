package dev.zalaris.alumni.common.sap;

public class PayslipDocumentNotFoundException extends RuntimeException {

  private final String name;
  private final String sequenceId;

  public PayslipDocumentNotFoundException(String name, String sequenceId) {
    this.name = name;
    this.sequenceId = sequenceId;
  }

  public String getName() {
    return name;
  }

  public String getSequenceId() {
    return sequenceId;
  }
}
