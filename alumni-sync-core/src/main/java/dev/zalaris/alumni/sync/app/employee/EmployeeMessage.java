package dev.zalaris.alumni.sync.app.employee;

import java.time.LocalDateTime;

record EmployeeMessage(String ssid, String zalarisId, LocalDateTime expirationDate) {}

