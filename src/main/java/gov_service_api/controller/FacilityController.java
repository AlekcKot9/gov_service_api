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

    @GetMapping("/getAll")
    public ResponseEntity<List<FacilityGetDTO>> getAll() {
        return new ResponseEntity<>(facilityService.getAllFacilities(), HttpStatus.OK);
    }

    @GetMapping("/getById")
    public ResponseEntity<List<InvoiceDTO>> getById(@RequestParam Long numFacility) {
        return new ResponseEntity<>(facilityService.getInvloiceBy(numFacility), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<FacilityGetDTO> create(@RequestBody FacilitySetDTO facilitySetDTO) {

        FacilityGetDTO facilityGetDTO = facilityService.create(facilitySetDTO);

        if (facilityGetDTO == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(facilityGetDTO);
    }
}
