package zimttech.org.diabetic.screening.app.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import zimttech.org.diabetic.screening.app.entity.VitalSigns;
import zimttech.org.diabetic.screening.app.entity.dto.VitalSignsDto;
import zimttech.org.diabetic.screening.app.entity.enums.VitalSignsType;
import zimttech.org.diabetic.screening.app.service.VitalSignService;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VitalSignsControllerTest {

    @Mock
    private VitalSignService vitalSignService;

    @InjectMocks
    private VitalSignsController vitalSignsController;

    @Test
    @DisplayName("Should record vitals and return created status with recorded vitals")
    void recordVitalsWhenValidInputThenReturnCreatedStatusAndRecordedVitals() {
        VitalSignsDto vitalSignsDto =
                new VitalSignsDto("patient123", VitalSignsType.TEMPERATURE, "37.5");

        VitalSigns vitalSigns =
                VitalSigns.builder()
                        .id(UUID.randomUUID().toString())
                        .patientId(vitalSignsDto.getPatientId())
                        .vitalSignsType(vitalSignsDto.getVitalSignsType())
                        .currentValue(vitalSignsDto.getCurrentValue())
                        .unitOfMeasurement(vitalSignsDto.getVitalSignsType().getUnitOfMeasurement())
                        .localDateOfVitalSign(LocalDateTime.now()) // set the localDateOfVitalSign field to the current date and time
                        .build();

        Map<String, Object> recordedVitals =
                Map.of(
                        "id", vitalSigns.getId(),
                        "patientId", vitalSigns.getPatientId(),
                        "vitalSignsType", vitalSigns.getVitalSignsType(),
                        "localDateOfVitalSign", vitalSigns.getLocalDateOfVitalSign(),
                        "currentValue", vitalSigns.getCurrentValue(),
                        "unitOfMeasurement", vitalSigns.getUnitOfMeasurement());

        when(vitalSignService.recordVitals(vitalSignsDto)).thenReturn(recordedVitals);

        ResponseEntity<Map<String, Object>> responseEntity =
                vitalSignsController.recordVitals(vitalSignsDto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(recordedVitals, responseEntity.getBody());
        verify(vitalSignService, times(1)).recordVitals(vitalSignsDto);
    }
}
