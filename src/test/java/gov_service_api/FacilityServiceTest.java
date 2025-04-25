package gov_service_api;

import gov_service_api.dto.FacilityGetDTO;
import gov_service_api.dto.FacilitySetDTO;
import gov_service_api.dto.user.InvoiceDTO;
import gov_service_api.model.Facility;
import gov_service_api.model.Invoice;
import gov_service_api.repository.FacilityRepository;
import gov_service_api.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacilityServiceTest {

    @Mock
    private FacilityRepository facilityRepository;

    @InjectMocks
    private FacilityService facilityService;

    private Facility testFacility;
    private FacilitySetDTO testSetDTO;

    @BeforeEach
    void setUp() {
        testFacility = new Facility();
        testFacility.setId(1L);
        testFacility.setName("Test Facility");
        testFacility.setPrice(1000.0);

        testSetDTO = new FacilitySetDTO();
        testSetDTO.setName("New Facility");
        testSetDTO.setPrise(1500.0);
    }

    @Test
    void getAllFacilities_ReturnsListOfDTOs() {
        // Arrange
        when(facilityRepository.findAll()).thenReturn(Collections.singletonList(testFacility));

        // Act
        List<FacilityGetDTO> result = facilityService.getAllFacilities();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Test Facility", result.get(0).getName());
        verify(facilityRepository).findAll();
    }

    @Test
    void getInvloiceBy_WithExistingFacility_ReturnsInvoices() {
        // Arrange
        Invoice testInvoice = new Invoice();
        testInvoice.setId(1L);
        testFacility.setInvoices(Collections.singletonList(testInvoice));

        when(facilityRepository.findById(1L)).thenReturn(Optional.of(testFacility));

        // Act
        List<InvoiceDTO> result = facilityService.getInvloiceBy(1L);

        // Assert
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void create_WithExistingName_ReturnsNull() {
        // Arrange
        when(facilityRepository.findByName("Existing Facility")).thenReturn(Optional.of(testFacility));
        testSetDTO.setName("Existing Facility");

        // Act
        FacilityGetDTO result = facilityService.create(testSetDTO);

        // Assert
        assertNull(result);
        verify(facilityRepository, never()).save(any(Facility.class));
    }

    @Test
    void create_ShouldSetCorrectFields() {
        // Arrange
        FacilitySetDTO testSetDTOss = new FacilitySetDTO();
        testSetDTOss.setName("New Facility");
        testSetDTOss.setPrise(1500.0);

        ArgumentCaptor<Facility> facilityCaptor = ArgumentCaptor.forClass(Facility.class);

        when(facilityRepository.findByName("New Facility"))
                .thenReturn(Optional.empty()) // Первый вызов
                .thenReturn(Optional.of(new Facility(testSetDTOss))); // Второй вызов
        when(facilityRepository.save(facilityCaptor.capture())).thenAnswer(invocation -> {
            Facility f = invocation.getArgument(0);
            f.setId(1L); // Эмулируем присвоение ID
            return f;
        });

        // Act
        facilityService.create(testSetDTOss);

        // Assert
        Facility capturedFacility = facilityCaptor.getValue();
        assertEquals("New Facility", capturedFacility.getName(), "Название не совпадает");
        assertEquals(1500.0, capturedFacility.getPrice(), "Цена не совпадает");
        assertNotNull(capturedFacility.getInvoices(), "Список счетов должен быть инициализирован");
        assertTrue(capturedFacility.getInvoices().isEmpty(), "Список счетов должен быть пустым");
    }
}