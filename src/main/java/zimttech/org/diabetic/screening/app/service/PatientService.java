package zimttech.org.diabetic.screening.app.service;

import zimttech.org.diabetic.screening.app.entity.Patient;
import zimttech.org.diabetic.screening.app.entity.dto.PatientDto;

import java.time.LocalDate;
import java.util.List;

public interface PatientService {
    Patient registerPatient(PatientDto patient);
    String deletePatient(String patientId);

    Patient updatePatient(String patientId, PatientDto patientDto);

    List<Patient> getPatients();

}
