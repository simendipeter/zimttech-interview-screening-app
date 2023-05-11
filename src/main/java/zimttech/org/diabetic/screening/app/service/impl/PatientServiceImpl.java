package zimttech.org.diabetic.screening.app.service.impl;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import zimttech.org.diabetic.screening.app.entity.Patient;
import zimttech.org.diabetic.screening.app.entity.dto.PatientDto;
import zimttech.org.diabetic.screening.app.repository.PatientRepository;
import zimttech.org.diabetic.screening.app.service.PatientService;
import zimttech.org.diabetic.screening.app.service.utils.FhirPatientConverter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Slf4j
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;;
    private final FhirPatientConverter fhirPatientConverter;

    private final RestTemplate restTemplate;

    public PatientServiceImpl(PatientRepository patientRepository, FhirPatientConverter fhirPatientConverter, RestTemplate restTemplate) {
        this.patientRepository = patientRepository;
        this.fhirPatientConverter = fhirPatientConverter;
        this.restTemplate = restTemplate;
    }

    @Override
    @Transactional
    public Patient registerPatient(@Valid PatientDto patientDto) {
        log.info("Registering patient: {}", patientDto);
        // Check if the patient is already registered
        if (patientRepository.existsByFirstNameAndLastNameAndDateOfBirth(
                patientDto.getFirstName(), patientDto.getLastName(), patientDto.getDateOfBirth())) {
            throw new ValidationException("Patient with the same first name, last name, and date of birth already exists.");
        }
        Patient patient = convertDtoToPatient(patientDto);
        Patient savedPatient = patientRepository.saveAndFlush(patient);
        log.info("Registered patient: {}", savedPatient);

        fhirPatientConverter.pushPatientToFhirServer(savedPatient);
        return savedPatient;
    }

    @Override
    public String deletePatient(String id) {
        Optional<Patient> patientOptional = patientRepository.findById(id);
        if (patientOptional.isEmpty()) {
            throw new IllegalArgumentException("Patient with ID " + id + " not found");
        }
        patientRepository.deleteById(id);
        return id;
    }


    @Override
    public Patient updatePatient(String patientId, @Valid PatientDto patientDto) {
        log.info("Updating patient with ID: {}", patientId);
        Patient existingPatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with ID: " + patientId));
        updatePatientFromDto(existingPatient, patientDto);
        Patient updatedPatient = patientRepository.save(existingPatient);
        log.info("Updated patient: {}", updatedPatient);
        return updatedPatient;
    }

    private void updatePatientFromDto(Patient patient, PatientDto patientDto) {
        patient.setFirstName(patientDto.getFirstName());
        patient.setLastName(patientDto.getLastName());
        patient.setSex(patientDto.getSex());
        patient.setDateOfBirth(patientDto.getDateOfBirth());
        patient.setHivAidsRecordNumber(patientDto.getHivAidsRecordNumber());
    }

    @Override
    public List<Patient> getPatients() {
        log.info("Getting patients from external API");
        String url = "http://localhost:8092/api/v1/patients";
        Patient[] patients = restTemplate.getForObject(url, Patient[].class);
        List<Patient> patientList = Arrays.asList(patients != null ? patients : new Patient[0]);
        patientRepository.saveAll(patientList);
        return patientList;
    }

    private Patient convertDtoToPatient(PatientDto
                                                patientDto) {
        return Patient.builder()
                .id(UUID.randomUUID().toString())
                .firstName(patientDto.getFirstName())
                .lastName(patientDto.getLastName())
                .sex(patientDto.getSex())
                .dateOfBirth(patientDto.getDateOfBirth())
                .hivAidsRecordNumber(patientDto.getHivAidsRecordNumber())
                .dateStartedTreatment(patientDto.getDateStartedTreatment())
                .build();
    }

}
