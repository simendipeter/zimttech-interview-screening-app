package zimttech.org.diabetic.screening.app.service.impl;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zimttech.org.diabetic.screening.app.entity.Patient;
import zimttech.org.diabetic.screening.app.entity.dto.PatientDto;
import zimttech.org.diabetic.screening.app.entity.enums.Gender;
import zimttech.org.diabetic.screening.app.repository.PatientRepository;
import zimttech.org.diabetic.screening.app.service.utils.FhirPatientConverter;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Patient Service")
class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private FhirPatientConverter fhirPatientConverter;

    @InjectMocks
    private PatientServiceImpl patientService;

    @Test
    @DisplayName("Should throw an exception when the patient ID is not found")
    void updatePatientWhenPatientIdNotFoundThenThrowException() {
        String patientId = "123";
        PatientDto patientDto =
                PatientDto.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .sex(Gender.MALE)
                        .dateOfBirth(LocalDate.of(1990, 1, 1))
                        .hivAidsRecordNumber("123456")
                        .dateStartedTreatment(LocalDate.of(2021, 1, 1))
                        .build();

        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> patientService.updatePatient(patientId, patientDto));

        verify(patientRepository, times(1)).findById(patientId);
    }

    @Test
    @DisplayName("Should update the patient when the patient ID is valid")
    void updatePatientWhenPatientIdIsValid() {
        String patientId = "123";
        PatientDto patientDto =
                new PatientDto(
                        "John",
                        "Doe",
                        Gender.MALE,
                        LocalDate.of(1990, 1, 1),
                        "123456",
                        LocalDate.of(2021, 1, 1));
        Patient existingPatient =
                Patient.builder()
                        .id(patientId)
                        .firstName("Jane")
                        .lastName("Doe")
                        .sex(Gender.FEMALE)
                        .dateOfBirth(LocalDate.of(1995, 1, 1))
                        .hivAidsRecordNumber("654321")
                        .dateStartedTreatment(LocalDate.of(2021, 1, 1))
                        .build();
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(existingPatient));
        when(patientRepository.save(existingPatient)).thenReturn(existingPatient);

        Patient updatedPatient = patientService.updatePatient(patientId, patientDto);

        assertNotNull(updatedPatient);
        assertEquals(patientDto.getFirstName(), updatedPatient.getFirstName());
        assertEquals(patientDto.getLastName(), updatedPatient.getLastName());
        assertEquals(patientDto.getSex(), updatedPatient.getSex());
        assertEquals(patientDto.getDateOfBirth(), updatedPatient.getDateOfBirth());
        assertEquals(patientDto.getHivAidsRecordNumber(), updatedPatient.getHivAidsRecordNumber());
        assertEquals(
                patientDto.getDateStartedTreatment(), updatedPatient.getDateStartedTreatment());
        verify(patientRepository, times(1)).findById(patientId);
        verify(patientRepository, times(1)).save(existingPatient);
    }

    @Test
    @DisplayName("Should throw an exception when the patient ID is not found")
    void deletePatientWhenIdNotFoundThenThrowException() {
        String id = "123";
        when(patientRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> patientService.deletePatient(id));

        verify(patientRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should delete the patient with the given ID")
    void deletePatientWithGivenId() {
        String patientId = "1234";
        Patient patient =
                Patient.builder()
                        .id(patientId)
                        .firstName("John")
                        .lastName("Doe")
                        .sex(Gender.MALE)
                        .dateOfBirth(LocalDate.of(1990, 1, 1))
                        .hivAidsRecordNumber("123456")
                        .build();

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        String deletedPatientId = patientService.deletePatient(patientId);

        assertNotNull(deletedPatientId);
        assertEquals(patientId, deletedPatientId);
        verify(patientRepository, times(1)).deleteById(patientId);
    }

    @Test
    @DisplayName(
            "Should throw a validation exception when a patient with the same first name, last name, and date of birth already exists")
    void
    registerPatientWhenPatientWithSameFirstNameLastNameAndDateOfBirthExistsThenThrowValidationException() {
        PatientDto patientDto =
                new PatientDto("John", "Doe", Gender.FEMALE, LocalDate.of(1990, 1, 1), "7777", LocalDate.of(1990, 1, 1));
        when(patientRepository.existsByFirstNameAndLastNameAndDateOfBirth(
                patientDto.getFirstName(),
                patientDto.getLastName(),
                patientDto.getDateOfBirth()))
                .thenReturn(true);

        assertThrows(ValidationException.class, () -> patientService.registerPatient(patientDto));

        verify(patientRepository, times(0)).saveAndFlush(any());
        verify(fhirPatientConverter, times(0)).pushPatientToFhirServer(any());
    }

    @Test
    @DisplayName(
            "Should register a new patient when the first name, last name, and date of birth are unique")
    void
    registerPatientWhenFirstNameLastNameAndDateOfBirthAreUnique() { // Create a new
        // patientDto object
        PatientDto patientDto =
                new PatientDto(
                        "John",
                        "Doe",
                        Gender.FEMALE,
                        LocalDate.of(1990, 1, 1),
                        "44444",
                        LocalDate.of(2021, 1, 1));

        // Mock the patientRepository to return false when checking if patient with same first name,
        // last name, and date of birth exists
        when(patientRepository.existsByFirstNameAndLastNameAndDateOfBirth(
                patientDto.getFirstName(),
                patientDto.getLastName(),
                patientDto.getDateOfBirth()))
                .thenReturn(false);

        // Mock the patientRepository to return the saved patient when saving a patient
        Patient patient =
                new Patient(
                        "1",
                        "John",
                        "Doe",
                        Gender.FEMALE,
                        LocalDate.of(1990, 1, 1),
                        "1234567890",
                        LocalDate.of(2021, 1, 1));
        when(patientRepository.saveAndFlush(any(Patient.class))).thenReturn(patient);

        // Call the registerPatient method with the patientDto object
        Patient savedPatient = patientService.registerPatient(patientDto);

        // Verify that the patientRepository's existsByFirstNameAndLastNameAndDateOfBirth method was
        // called once with the correct parameters
        verify(patientRepository, times(1))
                .existsByFirstNameAndLastNameAndDateOfBirth(
                        patientDto.getFirstName(),
                        patientDto.getLastName(),
                        patientDto.getDateOfBirth());

        // Verify that the patientRepository's saveAndFlush method was called once with the correct
        // patient object
        verify(patientRepository, times(1)).saveAndFlush(any(Patient.class));

        // Verify that the fhirPatientConverter's pushPatientToFhirServer method was called once
        // with the correct patient object
        verify(fhirPatientConverter, times(1)).pushPatientToFhirServer(patient);

        // Assert that the saved patient object is not null and has the correct values
        assertNotNull(savedPatient);
        assertEquals(patient.getId(), savedPatient.getId());
        assertEquals(patient.getFirstName(), savedPatient.getFirstName());
        assertEquals(patient.getLastName(), savedPatient.getLastName());
        assertEquals(patient.getDateOfBirth(), savedPatient.getDateOfBirth());
        assertEquals(patient.getSex(), savedPatient.getSex());
        assertEquals(patient.getHivAidsRecordNumber(), savedPatient.getHivAidsRecordNumber());
        assertEquals(patient.getDateStartedTreatment(), savedPatient.getDateStartedTreatment());
    }
}
