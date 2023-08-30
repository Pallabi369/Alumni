package dev.zalaris.alumni.app.infra.access;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ClaimAccessControlTests {

  @Test
  void whenNeitherSsidNorZalarisIdClaims_expectAllFalse() {
    var service = mock(ClaimAccessor.class);
    var accessControl = new ClaimAccessControl(service);
    assertFalse(accessControl.hasZalarisId("*"));
    assertFalse(accessControl.hasSsid("*"));
    assertFalse(accessControl.zalarisIdOrSsidClaimExists("*"));
    assertFalse(accessControl.ssidOrZalarisIdExists("*"));
  }

  @Test
  void whenZalarisIdExist() {
    var service = mock(ClaimAccessor.class);
    when(service.get(ClaimAccessor.ZALARIS_ID)).thenReturn(Optional.of("111"));
    var accessControl = new ClaimAccessControl(service);
    assertTrue(accessControl.hasZalarisId("111"));
    assertFalse(accessControl.hasZalarisId("*"));
    assertTrue(accessControl.zalarisIdOrSsidClaimExists("111"));
    assertFalse(accessControl.zalarisIdOrSsidClaimExists("*"));
    assertTrue(accessControl.ssidOrZalarisIdExists("*"));
  }

  @Test
  void whenSsidExists() {
    var service = mock(ClaimAccessor.class);
    when(service.get(ClaimAccessor.SSID)).thenReturn(Optional.of("111"));
    var accessControl = new ClaimAccessControl(service);
    assertFalse(accessControl.hasZalarisId("111"));
    assertTrue(accessControl.zalarisIdOrSsidClaimExists("*"));
    assertTrue(accessControl.hasSsid("111"));
    assertFalse(accessControl.hasSsid("*"));
    assertTrue(accessControl.ssidOrZalarisIdExists("111"));
    assertFalse(accessControl.ssidOrZalarisIdExists("*"));
  }

}
