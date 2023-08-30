package dev.zalaris.alumni.app.payroll.domain;

import lombok.Data;

import java.util.List;

@Data
public class PayrollResult {

  String sequence;
  String area;
  String inPeriod;
  String forPeriod;
  String startPayroll;
  String endPayroll;
  String companyCode;
  String payDate;
  String amountPaid;
  String currency;
  List<PayrollResultEntry> payrollResultsDetails;
  List<PayrollResultEntry> cumulativePayrollResults;
  List<BankTransfer> bankTransfers;
}
