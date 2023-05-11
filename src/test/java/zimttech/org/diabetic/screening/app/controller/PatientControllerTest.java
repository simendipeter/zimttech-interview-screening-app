package zimttech.org.diabetic.screening.app.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import zimttech.org.diabetic.screening.app.entity.Patient;
import zimttech.org.diabetic.screening.app.entity.dto.PatientDto;
import zimttech.org.diabetic.screening.app.entity.enums.Gender;
import zimttech.org.diabetic.screening.app.service.PatientService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientControllerTest {

    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    @Test
    @DisplayName("Should not register a patient with invalid input and return bad request status")
    void registerPatientWithInvalidInputReturnsBadRequestStatus() {
        PatientDto patientDto = new PatientDto("", "", null, null, "", null);

        ResponseEntity<Patient> responseEntity = patientController.registerPatient(patientDto);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(patientService, never()).registerPatient(patientDto);
    }

    @Test
    @DisplayName("Should register a patient with valid input and return created status")
    void registerPatientWithValidInputReturnsCreatedStatus() {
        PatientDto patientDto =
                new PatientDto(
                        "John",
                        "Doe",
                        Gender.MALE,
                        LocalDate.of(1990, 1, 1),
                        "HIV123",
                        LocalDate.of(2021, 1, 1));

        Patient patient =
                Patient.builder()
                        .firstName(patientDto.getFirstName())
                        .lastName(patientDto.getLastName())
                        .sex(patientDto.getSex())
                        .dateOfBirth(patientDto.getDateOfBirth())
                        .hivAidsRecordNumber(patientDto.getHivAidsRecordNumber())
                        .dateStartedTreatment(patientDto.getDateStartedTreatment())
                        .build();

        when(patientService.registerPatient(patientDto)).thenReturn(patient);

        ResponseEntity<Patient> response = patientController.registerPatient(patientDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(patient, response.getBody());
        verify(patientService, times(1)).registerPatient(patientDto);
    }
}
