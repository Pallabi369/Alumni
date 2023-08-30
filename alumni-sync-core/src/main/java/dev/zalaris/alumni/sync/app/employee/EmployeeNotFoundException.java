package dev.zalaris.alumni.sync.app.employee;

public class EmployeeNotFoundException extends RuntimeException {

  public EmployeeNotFoundException(String msg) {
    super(msg);
  }
}
