package dev.zalaris.alumni.app.payroll.usecase;

import dev.zalaris.alumni.app.payroll.domain.PayrollResultsModel;
import dev.zalaris.alumni.common.domain.Employee;

public interface PayrollResultsOutgoingPort {

  PayrollResultsModel handle(Employee employee);
}
