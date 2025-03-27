package gov_service_api.service;

import gov_service_api.dto.*;
import gov_service_api.model.*;
import gov_service_api.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class GovAgencyService {

    private GovAgencyRepository govAgencyRepository;
    private FacilityRepository facilityRepository;

    public GovAgencyService(GovAgencyRepository govAgencyRepository,
                            FacilityRepository facilityRepository) {
        this.govAgencyRepository = govAgencyRepository;
        this.facilityRepository = facilityRepository;
    }

    public boolean create(GovAgencyDTO govAgencyDTO) {

        boolean ans = govAgencyRepository.existsByName(govAgencyDTO.getName());

        if (!ans) {

            GovAgency govAgency = new GovAgency(
                    govAgencyDTO.getName(),
                    govAgencyDTO.getType(),
                    govAgencyDTO.getAddress()
            );

            govAgencyRepository.save(govAgency);

            return true;
        }

        return false;
    }

    public List<GovAgencyDTO> getAll() {

        List<GovAgency> govAgencyList = govAgencyRepository.findAll();

        List<GovAgencyDTO> govAgencyDTOList = new ArrayList<>();

        for (GovAgency govAgency : govAgencyList) {
            GovAgencyDTO govAgencyDTO = new GovAgencyDTO(
                    govAgency.getName(),
                    govAgency.getType(),
                    govAgency.getAddress()
            );
            govAgencyDTOList.add(govAgencyDTO);
        }

        return govAgencyDTOList;
    }

    public List<FacilityDTO> getFacilities(String name) {

        Optional<GovAgency> govAgencyOptional = govAgencyRepository.findByName(name);

        if (govAgencyOptional.isPresent()) {
            GovAgency govAgency = govAgencyOptional.get();

            List<FacilityDTO> facilitiesDTO = new ArrayList<>();

            for (Facility facility : govAgency.getFacilities()) {

                FacilityDTO facilityDTO = new FacilityDTO(
                        facility.getId(),
                        facility.getName(),
                        facility.getPrice()
                );

                facilitiesDTO.add(facilityDTO);
            }

            return facilitiesDTO;
        }

        throw new EmptyStackException();
    }

    public boolean addFacility(FacilityGogAgencyDTO facilityGogAgencyDTO) {

        Optional<GovAgency> govAgencyOptional =
                govAgencyRepository.findByName(facilityGogAgencyDTO.getGovAgencyName());

        if (govAgencyOptional.isPresent()) {

            GovAgency govAgency = govAgencyOptional.get();

            for (Facility facility : govAgency.getFacilities()) {
                if (facility.getName().equals(facilityGogAgencyDTO.getFacilityName())) {
                    return false;
                }
            }

            Facility facility = new Facility(
                    facilityGogAgencyDTO.getFacilityName(),
                    facilityGogAgencyDTO.getPrice()
            );
            facilityRepository.save(facility);

            List<Facility> facilities = govAgency.getFacilities();
            facilities.add(facility);
            govAgency.setFacilities(facilities);
            govAgencyRepository.save(govAgency);

            List<GovAgency> govAgencies = facility.getGovAgencies();
            govAgencies.add(govAgency);
            facility.setGovAgencies(govAgencies);
            facilityRepository.save(facility);

            return true;
        }

        return false;
    }

    public boolean delFacility(FacilityGogAgencyDTO facilityGogAgencyDTO) {

        Optional<GovAgency> govAgencyOptional =
                govAgencyRepository.findByName(facilityGogAgencyDTO.getGovAgencyName());

        if (govAgencyOptional.isPresent()) {

            GovAgency govAgency = govAgencyOptional.get();

            for (Facility facility : govAgency.getFacilities()) {

                if (facility.getName().equals(facilityGogAgencyDTO.getFacilityName())) {

                    List<Facility> facilities = govAgency.getFacilities();
                    facilities.remove(facility);
                    govAgency.setFacilities(facilities);
                    govAgencyRepository.save(govAgency);

                    List<GovAgency> govAgencies = facility.getGovAgencies();
                    govAgencies.remove(govAgency);

                    if (govAgencies.isEmpty()) {
                        facilityRepository.delete(facility);
                        return true;
                    }

                    facilityRepository.save(facility);

                    return true;
                }
            }
        }

        return false;
    }
}
