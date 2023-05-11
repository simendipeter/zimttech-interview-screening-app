package zimttech.org.diabetic.screening.app.service.utils;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zimttech.org.diabetic.screening.app.entity.VitalSigns;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;


@Service
@Slf4j
public class FhirPatientConverter {

    private final FhirContext fhirContext;;
    private final IGenericClient fhirClient;

    @Autowired
    public FhirPatientConverter(FhirContext fhirContext, IGenericClient fhirClient) {
        this.fhirContext = fhirContext;
        this.fhirClient = fhirClient;
    }

    public String convertToPatientResource(Patient patient) {
        FhirContext ctx = FhirContext.forR4();
        IParser parser = ctx.newJsonParser();
        return parser.encodeResourceToString(patient);
    }

    public Patient convertToPatient(zimttech.org.diabetic.screening.app.entity.Patient patient) {
        Patient fhirPatient = new Patient();
        fhirPatient.addName().setFamily(patient.getLastName()).addGiven(patient.getFirstName());
        fhirPatient.setGender(Enumerations.AdministrativeGender.valueOf(patient.getSex().toString()));
        fhirPatient.setBirthDate(convertToDate(patient.getDateOfBirth()));
        fhirPatient.setId(patient.getId());
        return fhirPatient;
    }

    public void pushPatientToFhirServer(zimttech.org.diabetic.screening.app.entity.Patient patient) {
        log.info("Pushing patient to FHIR server: {}", patient);
        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.TRANSACTION);

        Patient fhirPatient = convertToPatient(patient);
        bundle.addEntry().setResource(fhirPatient).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Patient");

        Bundle response = fhirClient.transaction().withBundle(bundle).execute();
        log.info("Pushed patient to FHIR server with response: {}", response);
    }

    private Observation convertVitalSignsToFhir(VitalSigns vitalSigns) {
        Observation observation = new Observation();
        observation.setId(vitalSigns.getId());
        observation.setStatus(Observation.ObservationStatus.FINAL);
        observation.getCode().addCoding()
                .setSystem("http://loinc.org")
                .setCode(vitalSigns.getVitalSignsType().toString())
                .setDisplay(vitalSigns.getVitalSignsType().name());
        observation.setValue(new Quantity()
                .setValue(0)
                .setUnit(vitalSigns.getUnitOfMeasurement()));
        observation.setSubject(new Reference("Patient/" + vitalSigns.getPatientId()));
        observation.setEffective(new DateTimeType(String.valueOf(vitalSigns.getLocalDateOfVitalSign())));
        return observation;
    }

    public void saveVitalSignsToServer(VitalSigns vitalSigns) {
        // Convert VitalSigns entity to FHIR resource
        Observation observation = convertVitalSignsToFhir(vitalSigns);

        // Push FHIR resource to HAPI FHIR server
        IGenericClient client = fhirContext.newRestfulGenericClient("http://localhost:8082/fhir");
        MethodOutcome outcome = client.create().resource(observation).execute();
    }

    private Date convertToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
