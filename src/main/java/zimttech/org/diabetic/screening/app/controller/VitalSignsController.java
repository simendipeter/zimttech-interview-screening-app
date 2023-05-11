package zimttech.org.diabetic.screening.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zimttech.org.diabetic.screening.app.entity.VitalSigns;
import zimttech.org.diabetic.screening.app.entity.dto.VitalSignsDto;
import zimttech.org.diabetic.screening.app.service.VitalSignService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/vitals")
public class VitalSignsController {

    private final VitalSignService vitalSignService;

    @Autowired
    public VitalSignsController(VitalSignService vitalSignService) {
        this.vitalSignService = vitalSignService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> recordVitals(@RequestBody VitalSignsDto dto) {
        Map<String, Object> recordedVitals = vitalSignService.recordVitals(dto);
        return new ResponseEntity<>(recordedVitals, HttpStatus.CREATED);
    }
}
