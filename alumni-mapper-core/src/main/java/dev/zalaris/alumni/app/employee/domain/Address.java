package dev.zalaris.alumni.app.employee.domain;

import lombok.Data;

@Data
public class Address {
    String addressType;
    String street;
    String street2ndLine;
    String houseNumber;
    String apartment;
    String postalCode;
    String city;
    String district;
    String state;
    String country;
    String phoneNumber;
}
