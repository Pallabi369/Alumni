package dev.zalaris.alumni.app.payroll.adapter.web;

import dev.zalaris.alumni.app.infra.access.ClaimAccessor;
import dev.zalaris.alumni.app.payroll.domain.PayrollResultsModel;
import dev.zalaris.alumni.app.payroll.usecase.FindPayrollResultsUseCase;
import dev.zalaris.alumni.app.payroll.usecase.FindPayslipFileUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;

import static dev.zalaris.alumni.app.infra.access.ClaimAccessor.SSID;

@RestController
@RequestMapping(path = "/api/employees")
@RequiredArgsConstructor
@Tag(name = "Payroll")
public class PayrollController {

  private final FindPayrollResultsUseCase findPayrollResultsUseCase;
  private final FindPayslipFileUseCase findPayslipFileUseCase;
  private final ClaimAccessor claimAccessor;

  @GetMapping(path = "/{zalarisId}/payroll-results")
  PayrollResultsModel getPayrollResults(@PathVariable @Pattern(regexp = "\\d{3}-\\d{8}") String zalarisId) {
    return findPayrollResultsUseCase.find(getSsid(), zalarisId);
  }

  @GetMapping(path = "/{zalarisId}/payroll-results/{sequenceId}/payslip", produces = MediaType.APPLICATION_PDF_VALUE)
  @ResponseBody
  ResponseEntity<InputStreamResource> getPayslip(@PathVariable @Pattern(regexp = "\\d{3}-\\d{8}") String zalarisId,
                                                 @PathVariable String sequenceId) {
    var payslipFile = findPayslipFileUseCase.find(getSsid(), zalarisId, sequenceId);
    return ResponseEntity.ok()
      .contentType(MediaType.APPLICATION_PDF)
      .body(new InputStreamResource(payslipFile.inputStream()));
  }

  private String getSsid() {
    return claimAccessor.<String>get(SSID).orElse(null);
  }

}
