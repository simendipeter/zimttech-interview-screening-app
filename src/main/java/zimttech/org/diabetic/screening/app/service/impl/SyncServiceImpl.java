package zimttech.org.diabetic.screening.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import zimttech.org.diabetic.screening.app.entity.Patient;
import zimttech.org.diabetic.screening.app.entity.VitalSigns;
import zimttech.org.diabetic.screening.app.repository.PatientRepository;
import zimttech.org.diabetic.screening.app.repository.VitalSignsRepository;
import zimttech.org.diabetic.screening.app.service.SyncService;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class SyncServiceImpl implements SyncService {

    private final PatientRepository patientRepository;
    private final VitalSignsRepository vitalSignsRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public SyncServiceImpl(PatientRepository patientRepository, VitalSignsRepository vitalSignsRepository, RestTemplate restTemplate) {
        this.patientRepository = patientRepository;
        this.vitalSignsRepository = vitalSignsRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Patient> getPatients() {
        log.info("Getting patients from external API");
        String url = "http://server:8092/api/v1/patients";
        Patient[] patients = restTemplate.getForObject(url, Patient[].class);
        List<Patient> patientList = Arrays.asList(patients != null ? patients : new Patient[0]);
        patientRepository.saveAll(patientList);
        return patientList;
    }

    @Override
    public List<VitalSigns> postVitals() {
        log.info("Getting vitals from database");
        List<VitalSigns> vitalList = vitalSignsRepository.findAll();
        log.info("Posting vitals to external API");
        String url = "http://server:8092/api/v1/vitals";
        restTemplate.postForObject(url, vitalList, Void.class);
        return vitalList;
    }
}
