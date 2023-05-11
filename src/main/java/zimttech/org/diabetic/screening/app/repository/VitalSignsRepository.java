package zimttech.org.diabetic.screening.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zimttech.org.diabetic.screening.app.entity.VitalSigns;

public interface VitalSignsRepository extends JpaRepository<VitalSigns, String> {
}
