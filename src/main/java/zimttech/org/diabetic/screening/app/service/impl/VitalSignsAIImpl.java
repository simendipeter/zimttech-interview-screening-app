package zimttech.org.diabetic.screening.app.service.impl;

import org.springframework.stereotype.Service;
import zimttech.org.diabetic.screening.app.entity.VitalSigns;
import zimttech.org.diabetic.screening.app.service.VitalSignsAIService;

import java.util.ArrayList;
import java.util.List;

@Service
public class VitalSignsAIImpl implements VitalSignsAIService {

    @Override
    public List<String> analyzeVitalSigns(VitalSigns vitalSigns) {
        List<String> notifications = new ArrayList<>();

        double currentValue = Double.parseDouble(vitalSigns.getCurrentValue());

        switch (vitalSigns.getVitalSignsType()) {
            case TEMPERATURE -> analyzeTemperature(currentValue, notifications);
            case BLOOD_PRESSURE -> analyzeBloodPressure(vitalSigns.getCurrentValue(), notifications);
            case BLOOD_SUGAR -> analyzeBloodSugar(currentValue, notifications);
        }

        return notifications;
    }

    private void analyzeTemperature(double temperature, List<String> notifications) {
        if (temperature < 36.1) {
            notifications.add("Low body temperature detected. Patient may have hypothermia.");
        } else if (temperature > 37.2) {
            notifications.add("High body temperature detected. Patient may have a fever.");
        } else {
            notifications.add("Normal body temperature detected.");
        }
    }

    @Override
    public String analyzeBMI(double weight, double height) {
        double heightInMeters = height / 100;
        double bmi = weight / (heightInMeters * heightInMeters);

        if (bmi < 18.5) {
           return  (String.format("Underweight detected. Patient's BMI is %.1f.", bmi));
        } else if (bmi >= 18.5 && bmi < 24.9) {
            return  (String.format("Normal weight detected. Patient's BMI is %.1f.", bmi));
        } else if (bmi >= 25 && bmi < 29.9) {
            return  (String.format("Overweight detected. Patient's BMI is %.1f.", bmi));
        } else {
            return  (String.format("Obesity detected. Patient's BMI is %.1f.", bmi));
        }
    }

    private void analyzeBloodPressure(String bloodPressure, List<String> notifications) {
        String[] bpValues = bloodPressure.split("/");
        int systolic = Integer.parseInt(bpValues[0]);
        int diastolic = Integer.parseInt(bpValues[1]);

        if (systolic < 90 || diastolic < 60) {
            notifications.add("Low blood pressure detected. Patient may have hypotension.");
        } else if (systolic > 140 || diastolic > 90) {
            notifications.add("High blood pressure detected. Patient may have hypertension.");
        } else {
            notifications.add("Normal blood pressure detected.");
        }
    }
    private void analyzeBloodSugar(double bloodSugar, List<String> notifications) {
        if (bloodSugar < 70) {
            notifications.add("Low blood sugar detected. Patient may have hypoglycemia.");
        } else if (bloodSugar > 180) {
            notifications.add("High blood sugar detected. Patient may have hyperglycemia.");
        } else {
            notifications.add("Normal blood sugar detected.");
        }
    }
}
