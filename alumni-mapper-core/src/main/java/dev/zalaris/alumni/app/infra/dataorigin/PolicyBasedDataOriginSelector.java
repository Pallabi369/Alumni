package dev.zalaris.alumni.app.infra.dataorigin;

import dev.zalaris.alumni.app.infra.access.ClaimAccessor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * PolicyBased implementation trusts 'tfp' claim to select the data source.
 */
@RequiredArgsConstructor
@EnableConfigurationProperties(DataOriginConfigurationProperties.class)
class PolicyBasedDataOriginSelector implements DataOriginSelector {

  private final ClaimAccessor claimAccessor;
  private final DataOriginConfigurationProperties properties;

  private static final String TRUST_FRAMEWORK_POLICY_CLAIM = "tfp";

  @Override
  public DataOrigin select() {
    var tfp = claimAccessor.<String>get(TRUST_FRAMEWORK_POLICY_CLAIM).orElseThrow(
      () -> new IllegalArgumentException("No '" + TRUST_FRAMEWORK_POLICY_CLAIM + "' claim found"));
    var sapPolicy = properties.getPolicy().getSapTarget();
    return sapPolicy.equalsIgnoreCase(tfp) ? DataOrigin.SAP_API : DataOrigin.ALUMNI;
  }

}
