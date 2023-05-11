package zimttech.org.diabetic.screening.app.service;

import zimttech.org.diabetic.screening.app.entity.VitalSigns;

import java.util.List;

public interface VitalSignsAIService {
    List<String> analyzeVitalSigns(VitalSigns vitalSigns);
    String analyzeBMI (double w, double h);
}
