package zimttech.org.diabetic.screening.app.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import zimttech.org.diabetic.screening.app.entity.Patient;
import zimttech.org.diabetic.screening.app.entity.enums.Gender;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

/**
 * A DTO for the {@link zimttech.org.diabetic.screening.app.entity.Patient} entity
 */
@Data
@AllArgsConstructor
@Builder
public class PatientDto implements Serializable {
    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 100, message = "First name must be between 1 and 100 characters")
    private final String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 100, message = "Last name must be between 1 and 100 characters")
    private final String lastName;

    @NotNull(message = "Gender is required")
    private final Gender sex;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private final LocalDate dateOfBirth;

    @Size(max = 100, message = "HIV/AIDS record number must be less than or equal to 100 characters")
    private final String hivAidsRecordNumber;

    @NotNull(message = "Date started treatment is required")
    @Past(message = "Date started treatment must be in the past")
    private final LocalDate dateStartedTreatment;

}
