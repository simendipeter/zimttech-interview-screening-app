package zimttech.org.diabetic.screening.app.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public FhirContext fhirContext() {
        return FhirContext.forR4();
    }
    @Bean
    public IGenericClient fhirClient() {
        FhirContext fhirContext = FhirContext.forR4();
        String serverBaseUrl = "http://localhost:8082/fhir";
        return fhirContext.newRestfulGenericClient(serverBaseUrl);
    }

}
