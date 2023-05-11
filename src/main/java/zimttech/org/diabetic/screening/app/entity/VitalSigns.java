package zimttech.org.diabetic.screening.app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zimttech.org.diabetic.screening.app.entity.enums.VitalSignsType;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class VitalSigns {
    @Id
    private String id;
    private String patientId;
    @Enumerated(EnumType.STRING)
    private VitalSignsType vitalSignsType;
    private LocalDateTime localDateOfVitalSign;
    private String currentValue;
    private String unitOfMeasurement;
}
