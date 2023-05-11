package zimttech.org.diabetic.screening.app.entity.enums;

public enum VitalSignsType {
    TEMPERATURE("°C"),
    BLOOD_PRESSURE("mmHg"),
    WEIGHT("kg"),
    HEIGHT("cm"),
    BLOOD_SUGAR("mg/dL");

    private final String unitOfMeasurement;

    VitalSignsType(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }
}
