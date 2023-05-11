package zimttech.org.diabetic.screening.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zimttech.org.diabetic.screening.app.entity.Patient;
import zimttech.org.diabetic.screening.app.entity.VitalSigns;
import zimttech.org.diabetic.screening.app.entity.dto.VitalSignsDto;
import zimttech.org.diabetic.screening.app.repository.PatientRepository;
import zimttech.org.diabetic.screening.app.repository.VitalSignsRepository;
import zimttech.org.diabetic.screening.app.service.VitalSignService;
import zimttech.org.diabetic.screening.app.service.VitalSignsAIService;
import zimttech.org.diabetic.screening.app.service.utils.FhirPatientConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class VitalSignsImpl implements VitalSignService {

    private final VitalSignsRepository vitalSignsRepository;
    private final VitalSignsAIService vitalSignsAIService;

    private final FhirPatientConverter fhirPatientConverter;
    private final PatientRepository patientRepository;

    public VitalSignsImpl(VitalSignsRepository vitalSignsRepository, VitalSignsAIService vitalSignsAIService, FhirPatientConverter fhirPatientConverter, PatientRepository patientRepository) {
        this.vitalSignsRepository = vitalSignsRepository;
        this.vitalSignsAIService = vitalSignsAIService;
        this.fhirPatientConverter = fhirPatientConverter;
        this.patientRepository = patientRepository;
    }

    @Override
    public Map<String, Object> recordVitals(VitalSignsDto dto) {
        log.info("Recording vital signs: {}", dto);

        Patient patient = getPatient(dto.getPatientId());

        if (patient.getDateStartedTreatment().plusMonths(18).isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Patient has not been on treatment for 18 months or more");
        }

        VitalSigns vitalSigns = convertDtoToVitalSigns(dto);
        VitalSigns savedVitalSigns = vitalSignsRepository.save(vitalSigns);
        log.info("Recorded vital signs: {}", savedVitalSigns);

        List<String> notifications = vitalSignsAIService.analyzeVitalSigns(savedVitalSigns);

        fhirPatientConverter.saveVitalSignsToServer(savedVitalSigns);

        return Map.of("vitalSigns", savedVitalSigns, "notifications", notifications);
    }

    private Patient getPatient(String patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
    }

    private VitalSigns convertDtoToVitalSigns(VitalSignsDto dto) {
        return VitalSigns.builder()
                .id(UUID.randomUUID().toString())
                .patientId(dto.getPatientId())
                .vitalSignsType(dto.getVitalSignsType())
                .localDateOfVitalSign(LocalDateTime.now())
                .currentValue(dto.getCurrentValue())
                .unitOfMeasurement(dto.getVitalSignsType().getUnitOfMeasurement())
                .build();
    }
}
