package dev.zalaris.alumni.sync.adapter.web;

import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
class UpdateDataExpirationRequestInputData {

  @NotBlank @Pattern(regexp = "\\d{3}-\\d{8}") private String zalarisId;
  @NotNull @Future private LocalDate expirationDate;

}
