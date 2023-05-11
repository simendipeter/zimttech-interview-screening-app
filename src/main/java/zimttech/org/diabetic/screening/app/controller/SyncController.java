package zimttech.org.diabetic.screening.app.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zimttech.org.diabetic.screening.app.entity.Patient;
import zimttech.org.diabetic.screening.app.entity.VitalSigns;
import zimttech.org.diabetic.screening.app.service.PatientService;
import zimttech.org.diabetic.screening.app.service.SyncService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sync")
public class SyncController {
    private final SyncService syncService;

    public SyncController(SyncService syncService) {
        this.syncService = syncService;
    }

    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> getPatientsFromServer() {
        List<Patient> patientList = syncService.getPatients();
        return new ResponseEntity<>(patientList, HttpStatus.OK);
    }

    @PostMapping("/vitals")
    public ResponseEntity<List<VitalSigns>> pushVitalsToServer() {
        List<VitalSigns> patientList = syncService.postVitals();
        return new ResponseEntity<>(patientList, HttpStatus.OK);
    }

}
