package gov_service_api.controller;

import gov_service_api.dto.*;
import gov_service_api.dto.user.*;
import gov_service_api.service.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/facility")
public class FacilityController {

    private FacilityService facilityService;

    public FacilityController(FacilityService facilityService) {
        this.facilityService = facilityService;
    }

    @GetMapping("/get")
    public ResponseEntity<List<FacilityDTO>> getFacilities() {
        return new ResponseEntity<>(facilityService.getAllFacilities(), HttpStatus.OK);
    }

    @GetMapping("/getById")
    public ResponseEntity<List<InvoiceDTO>> getById(@RequestParam Long numFacility) {
        return new ResponseEntity<>(facilityService.getInvloiceBy(numFacility), HttpStatus.OK);
    }
}
