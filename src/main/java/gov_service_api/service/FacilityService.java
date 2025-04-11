package gov_service_api.service;

import gov_service_api.dto.*;
import gov_service_api.dto.user.*;
import gov_service_api.model.*;
import gov_service_api.repository.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FacilityService {

    private final FacilityRepository facilityRepository;

    public FacilityService(FacilityRepository facilityRepository) {
        this.facilityRepository = facilityRepository;
    }

    public List<FacilityGetDTO> getAllFacilities() {

        List<Facility> facilities = facilityRepository.findAll(); //ГОРИТ КРАСНЫМ

        List<FacilityGetDTO> facilitiesDTO = new ArrayList<>();

        for (Facility facility : facilities) {
            FacilityGetDTO facilityDTO = new FacilityGetDTO(
                    facility.getId(),
                    facility.getName(),
                    facility.getPrice()
            );
            facilitiesDTO.add(facilityDTO);
        }

        return facilitiesDTO;
    }

    public List<InvoiceDTO> getInvloiceBy(Long numFacility) {

        Optional<Facility> facilityOptional = facilityRepository.findById(numFacility);

        Facility facility = facilityOptional.get();

        List<InvoiceDTO> invoicesDTO = new ArrayList<>();

        for (Invoice invoice : facility.getInvoices()) {
            InvoiceDTO invoiceDTO = new InvoiceDTO(
                    invoice.getId(),
                    invoice.getAmount(),
                    invoice.getRemainder(),
                    invoice.getStatus(),
                    invoice.getCreatedAt()
            );
            invoicesDTO.add(invoiceDTO);
        }

        return invoicesDTO;
    }

    public FacilityGetDTO create(FacilitySetDTO facilitySetDTO) {

        Optional<Facility> facilityOptional = facilityRepository.findByName(
                facilitySetDTO.getName());

        if (facilityOptional.isPresent()) {
            return null;
        }

        Facility facility = new Facility(facilitySetDTO);
        facilityRepository.save(facility);
        facilityOptional = facilityRepository.findByName(facility.getName());
        facility = facilityOptional.get();
        FacilityGetDTO facilityGetDTO = new FacilityGetDTO(facility);
        List<InvoiceDTO> invoicesDTO = new ArrayList<>();
        for (Invoice invoice : facility.getInvoices()) {
            invoicesDTO.add(new InvoiceDTO(invoice));
        }
        facilityGetDTO.setInvoicesDTO(invoicesDTO);
        facilityGetDTO.setId(facility.getId());
        return facilityGetDTO;
    }
}