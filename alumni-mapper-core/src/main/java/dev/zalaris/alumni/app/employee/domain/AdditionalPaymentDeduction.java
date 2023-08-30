package dev.zalaris.alumni.app.employee.domain;

import java.time.LocalDate;

public record AdditionalPaymentDeduction(String amount, String type, String currency, LocalDate startDate, LocalDate endDate) {
}
