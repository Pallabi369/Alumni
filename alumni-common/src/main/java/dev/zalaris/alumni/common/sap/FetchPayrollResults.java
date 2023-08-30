package dev.zalaris.alumni.common.sap;

import com.zalaris.api.nonsap.PayrollResult;

import java.util.List;

public interface FetchPayrollResults {

  List<PayrollResult> fetch(String zalarisId);

}
