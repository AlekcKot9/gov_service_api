package gov_service_api.controller;

import gov_service_api.dto.*;
import gov_service_api.dto.user.*;
import gov_service_api.service.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/facility")
@Tag(name = "Услуги", description = "Эндпоинты для работы с услугами")
public class FacilityController {

    private FacilityService facilityService;

    public FacilityController(FacilityService facilityService) {
        this.facilityService = facilityService;
    }

    @Operation(summary = "Получить все услуги")
    @GetMapping("/getAll")
    public ResponseEntity<List<FacilityGetDTO>> getAll() {
        return new ResponseEntity<>(facilityService.getAllFacilities(), HttpStatus.OK);
    }

    @Operation(summary = "Получить услугу по id")
    @GetMapping("/getById")
    public ResponseEntity<List<InvoiceDTO>> getById(@RequestParam Long numFacility) {
        return new ResponseEntity<>(facilityService.getInvloiceBy(numFacility), HttpStatus.OK);
    }

    @Operation(summary = "Создать услугу")
    @PostMapping("/create")
    public ResponseEntity<FacilityGetDTO> create(@RequestBody FacilitySetDTO facilitySetDTO) {

        FacilityGetDTO facilityGetDTO = facilityService.create(facilitySetDTO);

        if (facilityGetDTO == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(facilityGetDTO);
    }


}
