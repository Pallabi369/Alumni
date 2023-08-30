package dev.zalaris.alumni.app.infra.access;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static dev.zalaris.alumni.app.infra.access.ClaimAccessor.SSID;
import static dev.zalaris.alumni.app.infra.access.ClaimAccessor.ZALARIS_ID;

@RequiredArgsConstructor
@Slf4j
class ClaimAccessControl implements AccessControl {

  private final ClaimAccessor claimAccessor;

  private enum Access {
    DENIED, ABSTAINED, ALLOWED
  }

  @Override
  public boolean hasZalarisId(String zalarisId) {
    var result = claimAccessor.get(ZALARIS_ID)
      .map(claim -> claim.equals(zalarisId))
      .orElse(false);
    if (!result) {
      log.info("Access to zalarisId {}: {}", zalarisId, Access.DENIED.toString().toLowerCase());
    }
    return result;
  }

  public boolean zalarisIdOrSsidClaimExists(String zalarisId) {
    var access =  claimAccessor.get(ZALARIS_ID)
      .map(claim -> claim.equals(zalarisId) ? Access.ALLOWED : Access.DENIED)
      .orElse(claimAccessor.get(SSID).map(ssid -> Access.ABSTAINED).orElse(Access.DENIED));

    if (access == Access.DENIED) {
      log.info("Access to zalarisId {}: {}", zalarisId, access.toString().toLowerCase());
    } else {
      log.debug("Access to zalarisId {}: {}", zalarisId, access.toString().toLowerCase());
    }

    return access == Access.ALLOWED || access == Access.ABSTAINED;
  }

  public boolean ssidOrZalarisIdExists(String ssid) {
    var access = claimAccessor.get(SSID)
      .map(claim -> claim.equals(ssid) ? Access.ALLOWED : Access.DENIED)
      .orElse(claimAccessor.get(ZALARIS_ID).map(id -> Access.ABSTAINED).orElse(Access.DENIED));

    if (access == Access.DENIED) {
      log.info("Access to ssid {}: {}", ssid, access.toString().toLowerCase());
    } else {
      log.debug("Access to ssid {}: {}", ssid, access.toString().toLowerCase());
    }

    return access == Access.ALLOWED || access == Access.ABSTAINED;
  }

  @Override
  public boolean hasSsid(String ssid) {
    var result =  claimAccessor.get(SSID)
      .map(claim -> claim.equals(ssid))
      .orElse(false);
    if (!result) {
      log.info("Access to ssid {}: {}", ssid, Access.DENIED.toString().toLowerCase());
    }
    return result;
  }

}
