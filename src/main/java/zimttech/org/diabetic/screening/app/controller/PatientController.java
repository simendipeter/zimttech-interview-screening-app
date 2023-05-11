package zimttech.org.diabetic.screening.app.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zimttech.org.diabetic.screening.app.entity.Patient;
import zimttech.org.diabetic.screening.app.entity.dto.PatientDto;
import zimttech.org.diabetic.screening.app.service.PatientService;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<Patient> registerPatient(@Valid @RequestBody PatientDto patientDto) {
        if (patientDto.getFirstName().isEmpty() || patientDto.getLastName().isEmpty() || patientDto.getSex() == null || patientDto.getDateOfBirth() == null || patientDto.getHivAidsRecordNumber().isEmpty() || patientDto.getDateStartedTreatment() == null) {
            return new ResponseEntity<>(new Patient(), HttpStatus.BAD_REQUEST);
        }
        Patient savedPatient = patientService.registerPatient(patientDto);
        return new ResponseEntity<>(savedPatient, HttpStatus.CREATED);
    }

    @PutMapping("/{patientId}")
    public ResponseEntity<Patient> updatePatient(@PathVariable String patientId, @Valid @RequestBody PatientDto patientDto) {
        Patient updatedPatient = patientService.updatePatient(patientId, patientDto);
        return new ResponseEntity<>(updatedPatient, HttpStatus.OK);
    }

    @DeleteMapping("/{patientId}")
    public ResponseEntity<String> deletePatient(@PathVariable String patientId) {
        String deletedPatientId = patientService.deletePatient(patientId);
        return new ResponseEntity<>(deletedPatientId, HttpStatus.OK);
    }
}
