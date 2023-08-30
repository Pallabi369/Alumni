package dev.zalaris.alumni.app.payroll.domain;

import java.util.List;

public record PayrollResultsModel(String ssid, String zalarisId, List<PayrollResult> payrollResults) {
}
