package dev.zalaris.alumni.sync.app.payslip.port.inbound;

public interface CreatePayslipFileUseCase {

  void create(String ssid, String zalarisId, String fileIdentifier);

}
