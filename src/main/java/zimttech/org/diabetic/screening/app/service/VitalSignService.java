package zimttech.org.diabetic.screening.app.service;

import zimttech.org.diabetic.screening.app.entity.VitalSigns;
import zimttech.org.diabetic.screening.app.entity.dto.VitalSignsDto;

import java.util.List;
import java.util.Map;

public interface VitalSignService {
    Map<String, Object> recordVitals(VitalSignsDto dto);

}
