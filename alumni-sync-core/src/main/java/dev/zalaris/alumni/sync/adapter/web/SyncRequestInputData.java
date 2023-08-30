package dev.zalaris.alumni.sync.adapter.web;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
class SyncRequestInputData extends UpdateDataExpirationRequestInputData{

  @NotBlank private String ssid;

}
