package zimttech.org.diabetic.screening.app.service;

import zimttech.org.diabetic.screening.app.entity.Patient;
import zimttech.org.diabetic.screening.app.entity.VitalSigns;

import java.util.List;

public interface SyncService {

    List<Patient> getPatients();

    List<VitalSigns> postVitals();
}
