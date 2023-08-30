package dev.zalaris.alumni.app.infra.access;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("#ssid != null ? @accessControl.hasSsid(#ssid) : @accessControl.hasZalarisId(#zalarisId)")
public @interface EmployeeHistoryAccess {}
