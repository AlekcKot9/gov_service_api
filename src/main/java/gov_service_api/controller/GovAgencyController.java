package gov_service_api.controller;

import gov_service_api.dto.*;
import gov_service_api.service.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/govAgency")
public class GovAgencyController {

    private GovAgencyService govAgencyService;

    public GovAgencyController(GovAgencyService govAgencyService) {
        this.govAgencyService = govAgencyService;
    }

    @PostMapping("/create")
    public ResponseEntity<StringDTO> create(@RequestBody GovAgencyDTO govAgencyDTO) {

        if (govAgencyService.create(govAgencyDTO)) {
            return ResponseEntity.ok(new StringDTO("GovAgency create successfully"));
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new StringDTO("GovAgency already exists"));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<GovAgencyDTO>> getAll() {

        return ResponseEntity.ok(govAgencyService.getAll());
    }

    @GetMapping("/getFacilities")
    public ResponseEntity<List<FacilityDTO>> getFacilities(@RequestBody StringDTO name) {

        try {
            List<FacilityDTO> faciliteisDTO = govAgencyService.getFacilities(name.getMessage());
            return ResponseEntity.ok(faciliteisDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/addFacility")
    public ResponseEntity<StringDTO> addFacility(
            @RequestBody FacilityGogAgencyDTO facilityGogAgencyDTO) {

        if (govAgencyService.addFacility(facilityGogAgencyDTO)) {
            return ResponseEntity.ok(new StringDTO("Facility added successfully"));
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(new StringDTO("Facility not added"));
    }

    @DeleteMapping("/delFacility")
    public ResponseEntity<StringDTO> delFacility(
            @RequestBody FacilityGogAgencyDTO facilityGogAgencyDTO) {

        if (govAgencyService.delFacility(facilityGogAgencyDTO)) {
            return ResponseEntity.ok(new StringDTO("Facility deleted successfully"));
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new StringDTO("Facility not deleted"));
    }
}
