package dev.zalaris.alumni.common.sap;

import com.zalaris.api.nonsap.DTPAMasterdataResponse;

import java.util.Optional;

public interface FetchPersonalDataContainer {

  Optional<DTPAMasterdataResponse> fetch(String zalarisId);

}
