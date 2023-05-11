package zimttech.org.diabetic.screening.app.entity.dto;

import lombok.Data;
import zimttech.org.diabetic.screening.app.entity.enums.VitalSignsType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link zimttech.org.diabetic.screening.app.entity.VitalSigns} entity
 */
@Data
public class VitalSignsDto implements Serializable {
    private final String patientId;
    private final VitalSignsType vitalSignsType;
    private final String currentValue;
}
