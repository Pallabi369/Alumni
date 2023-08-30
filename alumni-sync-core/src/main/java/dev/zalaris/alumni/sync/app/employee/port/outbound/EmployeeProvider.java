package dev.zalaris.alumni.sync.app.employee.port.outbound;

import com.zalaris.api.nonsap.DTPAMasterdataResponse;
import com.zalaris.api.nonsap.PayrollResult;

import java.util.List;
import java.util.Optional;

public interface EmployeeProvider {

  Optional<DTPAMasterdataResponse> fetchMasterData(String zalarisId);

  List<PayrollResult> fetchPayrolls(String zalarisId);

}
