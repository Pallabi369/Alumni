package dev.zalaris.alumni.app.employee.domain;

import java.time.LocalDate;

public record RecurringPaymentDeduction(String amount, String type, String currency, LocalDate startDate, LocalDate endDate) {
}
