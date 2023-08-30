package dev.zalaris.alumni.app.employee.domain;

import java.util.List;

public record EmploymentHistoryModel(String ssid, List<Employment> employments) {
}
