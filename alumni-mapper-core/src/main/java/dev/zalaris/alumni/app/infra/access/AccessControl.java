package dev.zalaris.alumni.app.infra.access;

public interface AccessControl {

  boolean hasZalarisId(String zalarisId);

  boolean hasSsid(String ssid);

  boolean zalarisIdOrSsidClaimExists(String zalarisId);

  boolean ssidOrZalarisIdExists(String ssid);

}
