package dev.zalaris.alumni.app.payroll.adapter.mapper;

import com.zalaris.api.nonsap.BtResult;
import com.zalaris.api.nonsap.CrtResult;
import com.zalaris.api.nonsap.PayrollResult;
import com.zalaris.api.nonsap.RtResult;
import dev.zalaris.alumni.app.payroll.domain.BankTransfer;
import dev.zalaris.alumni.app.payroll.domain.PayrollResultEntry;
import dev.zalaris.alumni.app.payroll.domain.PayrollResultsModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface PayrollResultMapper {

  @Mapping(source = "lgartText", target = "wageType", defaultExpression = "java(input.getLgart())")
  @Mapping(source = "anzhl", target = "hrPayrollNumber")
  @Mapping(source = "betrg", target = "hrPayrollAmount")
  PayrollResultEntry mapToCumulativePayrollResult(CrtResult input);

  @Mapping(source = "lgartText", target = "wageType", defaultExpression = "java(input.getLgart())")
  @Mapping(source = "anzhl", target = "hrPayrollNumber")
  @Mapping(source = "betrg", target = "hrPayrollAmount")
  PayrollResultEntry mapToPayrollResultDetails(RtResult input);

  @Mapping(source = "lgartText", target = "wageType", defaultExpression = "java(input.getLgart())")
  @Mapping(source = "betrg", target = "hrPayrollAmount")
  @Mapping(source = "banks", target = "bankCountryKey")
  @Mapping(source = "bankl", target = "bankNumber")
  @Mapping(source = "bankn", target = "bankAccountNumber")
  @Mapping(source = "dtadt", target = "transferDate")
  @Mapping(source = "waers", target = "currency")
  BankTransfer mapToBankTransfer(BtResult input);


  @Mapping(source = "seqnr", target = "sequence")
  @Mapping(source = "abkrsText", target = "area")
  @Mapping(source = "inper", target = "inPeriod")
  @Mapping(source = "fpper", target = "forPeriod")
  @Mapping(source = "fpbeg", target = "startPayroll")
  @Mapping(source = "fpend", target = "endPayroll")
  @Mapping(source = "juperText", target = "companyCode")
  @Mapping(source = "paydt", target = "payDate")
  @Mapping(source = "amountPaid", target = "amountPaid")
  @Mapping(source = "amountPaidCurrency", target = "currency")
  @Mapping(source = "crtResults", target = "cumulativePayrollResults")
  @Mapping(source = "rtResults", target = "payrollResultsDetails")
  @Mapping(source = "btResults", target = "bankTransfers")
  dev.zalaris.alumni.app.payroll.domain.PayrollResult mapToPayrollResult(PayrollResult input);

  @Mapping(target = "ssid")
  @Mapping(target = "zalarisId")
  @Mapping(source = "input", target = "payrollResults")
  PayrollResultsModel mapToPayrollResultsModel(String ssid, String zalarisId, List<PayrollResult> input);

}
