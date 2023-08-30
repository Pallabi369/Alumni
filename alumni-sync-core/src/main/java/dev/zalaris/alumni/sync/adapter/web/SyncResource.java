package dev.zalaris.alumni.sync.adapter.web;

import dev.zalaris.alumni.common.web.error.Error;
import dev.zalaris.alumni.common.web.error.ErrorResponse;
import dev.zalaris.alumni.sync.app.employee.EmployeeMessageDeliveryException;
import dev.zalaris.alumni.sync.app.employee.port.inbound.CreateEmployeeUseCase;
import dev.zalaris.alumni.sync.app.employee.port.inbound.UpdateExpirationDateUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
class SyncResource {

  private final CreateEmployeeUseCase createEmployeeUseCase;

  private final UpdateExpirationDateUseCase updateExpirationDateUseCase;

  @PostMapping(path = "/sync", consumes = APPLICATION_JSON_VALUE)
  ResponseEntity<?> sync(@RequestBody @Valid SyncRequestInputData syncRequest) {
    try {
      MDC.put("zalarisId", syncRequest.getZalarisId());
      MDC.put("ssid", syncRequest.getSsid());
      log.info("Received sync data request: {}", syncRequest);

      createEmployeeUseCase.create(DigestUtils.sha256Hex(syncRequest.getSsid()),
        syncRequest.getZalarisId(), syncRequest.getExpirationDate().atStartOfDay());
      return ResponseEntity.status(HttpStatus.CREATED).build();
    } finally {
      MDC.clear();
    }
  }

  @ResponseStatus(HttpStatus.BAD_GATEWAY)
  @ExceptionHandler(EmployeeMessageDeliveryException.class)
  ErrorResponse handleException(HttpServletRequest request, EmployeeMessageDeliveryException exception) {
    return new ErrorResponse(HttpStatus.BAD_GATEWAY.value(), request.getRequestURI(),
      new Error(exception.getCode(), exception.getMessage()));
  }

  @PutMapping(path = "/expirationDate", consumes = APPLICATION_JSON_VALUE)
  ResponseEntity<?> updateDataExpiration(@RequestBody @Valid UpdateDataExpirationRequestInputData requestData) {
    try {
      MDC.put("zalarisId", requestData.getZalarisId());
      log.info("Received update Date Expiration request: {}", requestData);
      updateExpirationDateUseCase.updateDateExpiration(requestData.getZalarisId(), requestData.getExpirationDate().atStartOfDay());
      return ResponseEntity.status(HttpStatus.OK).build();
    }
    finally {
      MDC.clear();
    }
  }

}
