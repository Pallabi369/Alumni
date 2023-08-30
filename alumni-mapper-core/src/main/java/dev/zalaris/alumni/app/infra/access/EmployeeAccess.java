package dev.zalaris.alumni.app.infra.access;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@accessControl.zalarisIdOrSsidClaimExists(#zalarisId)")
@PostAuthorize("@accessControl.ssidOrZalarisIdExists(returnObject.ssid)")
public @interface EmployeeAccess {}
