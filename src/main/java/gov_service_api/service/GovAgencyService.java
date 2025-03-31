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

    public boolean create(SetGovAgencyDTO setGovAgencyDTO) {

        boolean ans = govAgencyRepository.existsByName(setGovAgencyDTO.getName());

        if (!ans) {

            GovAgency govAgency = new GovAgency(
                    setGovAgencyDTO.getName(),
                    setGovAgencyDTO.getType(),
                    setGovAgencyDTO.getAddress()
            );

            govAgencyRepository.save(govAgency);

            return true;
        }

        return false;
    }

    public List<GetGovAgencyDTO> getAll() {

        List<GovAgency> govAgencyList = govAgencyRepository.findAll();

        List<GetGovAgencyDTO> getGovAgencyDTOList = new ArrayList<>();

        for (GovAgency govAgency : govAgencyList) {
            List<FacilityDTO> facilitiesDTO  = new ArrayList<>();
            for(Facility facility : govAgency.getFacilities()) {
                FacilityDTO facilityDTO = new FacilityDTO(
                        facility.getId(),
                        facility.getName(),
                        facility.getPrice()
                );
                facilitiesDTO.add(facilityDTO);
            }

            GetGovAgencyDTO getGovAgencyDTO = new GetGovAgencyDTO(
                    govAgency.getName(),
                    govAgency.getType(),
                    govAgency.getAddress(),
                    facilitiesDTO
            );

            getGovAgencyDTOList.add(getGovAgencyDTO);
        }

        return getGovAgencyDTOList;
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

            Optional<Facility> facilityOptional = facilityRepository.findByName(facilityGogAgencyDTO.getFacilityName());

            Facility facility = new Facility();

            if (facilityOptional.isPresent()) {
                facility = facilityOptional.get();
            } else {
                facility = new Facility(
                        facilityGogAgencyDTO.getFacilityName(),
                        facilityGogAgencyDTO.getPrice()
                );
            }

            List<GovAgency> govAgencies = facility.getGovAgencies();
            govAgencies.add(govAgency);
            facility.setGovAgencies(govAgencies);
            facilityRepository.save(facility);

            List<Facility> facilities = govAgency.getFacilities();
            facilities.add(facility);
            govAgency.setFacilities(facilities);
            govAgencyRepository.save(govAgency);

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

    public List<GetGovAgencyByFacIdDTO> getByFacilityId(Long facilityId) {

        Optional<Facility> facilityOptional = facilityRepository.findById(facilityId);

        if(facilityOptional.isPresent()) {
            List<GovAgency> govAgencies = govAgencyRepository.findByFacilityId(facilityId);
            List<GetGovAgencyByFacIdDTO> getGovAgenciesByFacIdDTOList = new ArrayList<>();
            for(GovAgency govAgency : govAgencies) {
                getGovAgenciesByFacIdDTOList.add(new GetGovAgencyByFacIdDTO(govAgency));
            }
            return getGovAgenciesByFacIdDTOList;
        }

        throw new EmptyStackException();
    }
}
