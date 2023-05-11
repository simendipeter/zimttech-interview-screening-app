package zimttech.org.diabetic.screening.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zimttech.org.diabetic.screening.app.entity.Patient;

import java.time.LocalDate;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, String> {
    Optional<Patient> findByFirstNameAndLastNameAndDateOfBirth(String firstName, String lastName, LocalDate dateOfBirth);

    boolean existsByFirstNameAndLastNameAndDateOfBirth(String firstName, String lastName, LocalDate dateOfBirth);
}
