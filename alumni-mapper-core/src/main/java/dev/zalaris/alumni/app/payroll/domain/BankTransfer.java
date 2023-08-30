package dev.zalaris.alumni.app.payroll.domain;


public record BankTransfer(String wageType, String hrPayrollAmount, String bankCountryKey, String bankNumber, String bankAccountNumber, String transferDate, String currency) {
}
