package dev.zalaris.alumni.app.employee.domain;

import java.util.List;

public record PersonalDataModel(String ssid,
                                String zalarisId,
                                OrganizationalAssignmentGroups orgAssignment,
                                PersonalInformationGroups personalInformation,
                                List<Address> addresses,
                                List<Communication> communications,
                                BasicPayGroups basicPay,
                                List<RecurringPaymentDeduction> recurringPaymentDeductions,
                                List<AdditionalPaymentDeduction> additionalPaymentDeductions,
                                List<BankDetails> bankDetails
) {

  public record OrganizationalAssignmentGroups(Personnel personnel,
                                               EnterpriseStructure enterpriseStructure,
                                               PersonnelStructure personnelStructure,
                                               OrganizationalPlan organizationalPlan,
                                               Administrator administrator) {}

  public record PersonalInformationGroups(Personnel personnel,
                                          Name name,
                                          AdditionalData additionalData) {}

  public record BasicPayGroups(Personnel personnel,
                               Payscale payscale,
                               List<Paywage> payWages) {}
}
