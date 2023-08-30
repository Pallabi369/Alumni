package dev.zalaris.alumni.app.payroll.domain;

import java.io.InputStream;

public record PayslipFile(String ssid, String zalarisId, InputStream inputStream) {}
