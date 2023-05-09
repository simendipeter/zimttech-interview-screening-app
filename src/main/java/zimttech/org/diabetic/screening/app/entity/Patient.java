package zimttech.org.diabetic.screening.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Patient {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private Gender sex;
    private LocalDate dateOfBirth;
    private String hivAidsRecordNumber;

}
