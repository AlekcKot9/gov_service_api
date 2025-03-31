package gov_service_api.controller;

import gov_service_api.dto.*;
import gov_service_api.model.*;
import gov_service_api.repository.*;
import gov_service_api.service.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/govAgency")
public class GovAgencyController {

    private final FacilityRepository facilityRepository;
    private GovAgencyService govAgencyService;

    public GovAgencyController(GovAgencyService govAgencyService, FacilityRepository facilityRepository) {
        this.govAgencyService = govAgencyService;
        this.facilityRepository = facilityRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<StringDTO> create(@RequestBody SetGovAgencyDTO setGovAgencyDTO) {

        if (govAgencyService.create(setGovAgencyDTO)) {
            return ResponseEntity.ok(new StringDTO("GovAgency create successfully"));
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new StringDTO("GovAgency already exists"));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<GetGovAgencyDTO>> getAll() {

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

        System.out.println(facilityGogAgencyDTO);

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

    @PostMapping("/getByFacilityId")
    public ResponseEntity<List<GetGovAgencyByFacIdDTO>> getByFacilityId(
            @RequestBody LongDTO facilityId) {

        try {
            return ResponseEntity.ok(govAgencyService.getByFacilityId(facilityId.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
