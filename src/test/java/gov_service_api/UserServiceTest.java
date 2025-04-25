package gov_service_api;

import gov_service_api.dto.user.*;
import gov_service_api.exception.*;
import gov_service_api.model.*;
import gov_service_api.repository.*;
import gov_service_api.repository.cache.*;
import gov_service_api.security.PasswordUtil;
import gov_service_api.service.UserService;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*; // <-- правильно!
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String CLOSE = "close";

    @Mock
    private UserRepository userRepository;

    @Mock
    private FacilityRepository facilityRepository;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private UserDtoCache userDtoCache;

    @Mock
    private InvoiceGetDTOCache invoiceGetDTOCache;

    private MockHttpServletRequest request;

    @InjectMocks
    private UserService userService;

    private SignupDTO validSignupDTO;

    @BeforeEach
    void setUp() {
        validSignupDTO = new SignupDTO();
        validSignupDTO.setPersonalId("123456789012");
        validSignupDTO.setFirstName("John");
        validSignupDTO.setLastName("Doe");
        validSignupDTO.setPhoneNumber("+77071234567");
        validSignupDTO.setAddress("Astana");
        validSignupDTO.setPassword("Password123!");
        request = new MockHttpServletRequest();
    }

    @Test
    void signup_WithValidData_ReturnsTrueAndSavesUser() {
        when(userRepository.existsByPersonalId(validSignupDTO.getPersonalId())).thenReturn(false);

        boolean result = userService.signup(validSignupDTO);

        assertTrue(result);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals(validSignupDTO.getPersonalId(), savedUser.getPersonalId());
        assertTrue(PasswordUtil.checkPassword(
                validSignupDTO.getPassword(),
                savedUser.getPassword()
        ));
    }

    @Test
    void signup_WithExistingPersonalId_ThrowsException() {
        when(userRepository.existsByPersonalId(validSignupDTO.getPersonalId())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.signup(validSignupDTO);
        });
    }

    @Test
    void login_WithValidCredentials_ReturnsTrueAndSetsSession() {
        LoginDTO loginDTO = new LoginDTO("123", "pass");
        User user = new User();
        user.setPassword(PasswordUtil.hashPassword("pass"));

        when(userRepository.findByPersonalId("123")).thenReturn(user);

        boolean result = userService.login(loginDTO, request);

        assertTrue(result);
        assertNotNull(request.getSession().getAttribute("personalId"));
    }

    @Test
    void login_WithInvalidPassword_ReturnsFalse() {
        LoginDTO loginDTO = new LoginDTO("123", "wrong");
        User user = new User();
        user.setPassword(PasswordUtil.hashPassword("pass"));

        when(userRepository.findByPersonalId("123")).thenReturn(user);

        boolean result = userService.login(loginDTO, request);

        assertFalse(result);
        assertNull(request.getSession().getAttribute("personalId"));
    }

    @Test
    void logout_ClearsSessionAndCache() {
        HttpSession session = request.getSession();
        session.setAttribute("personalId", "123");

        boolean result = userService.logout(request);

        assertTrue(result);
        assertNull(session.getAttribute("personalId"));
        verify(userDtoCache).clearCache();
    }

    @Test
    void getProfile_WhenNotCached_FetchesFromRepositoryAndCaches() {
        User user = new User();
        user.setPersonalId("123");
        when(userDtoCache.isPresent("123")).thenReturn(false);
        when(userRepository.findByPersonalId("123")).thenReturn(user);

        UserGetDTO result = userService.getProfile("123");

        assertNotNull(result);
        verify(userDtoCache).putInCache(eq("123"), any(UserGetDTO.class));
    }

    @Test
    void addInvoice_WithValidFacility_SavesInvoiceAndUpdatesCache() {
        Long facilityId = 1L;
        Facility facility = new Facility();
        facility.setPrice(100.0);
        User user = new User();
        user.setInvoices(new ArrayList<>());

        when(facilityRepository.findById(facilityId)).thenReturn(Optional.of(facility));
        when(userRepository.findByPersonalId("123")).thenReturn(user);
        when(invoiceGetDTOCache.isPresent(123L)).thenReturn(true);

        boolean result = userService.addInvoice(facilityId, "123");

        assertTrue(result);
        verify(invoiceRepository).save(any(Invoice.class));
        verify(invoiceGetDTOCache).putInCache(eq(123L), anyList());
    }

    @Test
    void getInvoices_WhenNotCached_FetchesFromRepository() {
        User user = new User();
        Invoice invoice = new Invoice(100.0, "open");
        user.setInvoices(Collections.singletonList(invoice));

        when(invoiceGetDTOCache.isPresent(123L)).thenReturn(false);
        when(userRepository.findByPersonalId("123")).thenReturn(user);

        List<InvoiceDTO> result = userService.getInvoices(123L);

        assertEquals(1, result.size());
        verify(invoiceGetDTOCache).putInCache(eq(123L), anyList());
    }

    @Test
    void addPayment_WithValidData_UpdatesInvoiceAndBalance() {
        AddPaymentDTO dto = new AddPaymentDTO();
        dto.setInvoiceId(1L);
        dto.setAmount(50.0);
        Invoice invoice = new Invoice(100.0, "open");
        invoice.setRemainder(100.0);
        User user = new User();
        user.setBalance(100.0);

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        when(userRepository.findByPersonalId("123")).thenReturn(user);

        boolean result = userService.addPayment(dto, "123");

        assertTrue(result);
        assertEquals(50.0, invoice.getRemainder());
        assertEquals(50.0, user.getBalance());
    }

    @Test
    void getWithOpenInv_ReturnsUsersWithOpenInvoices() {
        User user = new User();
        when(userRepository.findByStatusInv("open")).thenReturn(Collections.singletonList(user));

        List<UserGetDTO> result = userService.getWithOpenInv("open");

        assertEquals(1, result.size());
        verify(userRepository).findByStatusInv("open");
    }

    @Test
    void delete_RemovesUserAndClearsDependencies() {
        User user = new User();
        user.setInvoices(new ArrayList<>());
        when(userRepository.findByPersonalId("123")).thenReturn(user);

        userService.delete("123");

        verify(userRepository).delete(user);
        verify(userDtoCache).clearCache();
    }

    @Test
    void getInvoicesByUserAndStatus_ReturnsFilteredInvoices() {
        Invoice invoice = new Invoice(100.0, "open");
        when(invoiceRepository.findByUserIdAndStatus(1L, "open"))
                .thenReturn(Collections.singletonList(invoice));

        List<InvoiceDTO> result = userService.getInvoicesByUserAndStatus(1L, "open");

        assertEquals(1, result.size());
        assertEquals("open", result.get(0).getStatus());
    }

    @Test
    void signupUsers_WithNewUsers_ReturnsSavedUsers() {
        // Устанавливаем все обязательные поля для DTO
        SignupDTO dto1 = new SignupDTO();
        dto1.setPersonalId("new1");
        dto1.setFirstName("John");
        dto1.setLastName("Doe");
        dto1.setPhoneNumber("+77071234567");
        dto1.setAddress("Astana");
        dto1.setPassword("Password123!");

        SignupDTO dto2 = new SignupDTO();
        dto2.setPersonalId("existing");
        dto2.setFirstName("Jane");
        dto2.setLastName("Smith");
        dto2.setPhoneNumber("+77076543210");
        dto2.setAddress("Almaty");
        dto2.setPassword("SecurePass456!");

        List<SignupDTO> dtoList = Arrays.asList(dto1, dto2);

        // Настраиваем моки
        when(userRepository.existsByPersonalId("new1")).thenReturn(false);
        when(userRepository.existsByPersonalId("existing")).thenReturn(true);

        // Захватываем аргумент для проверки
        ArgumentCaptor<List<User>> usersCaptor = ArgumentCaptor.forClass(List.class);
        when(userRepository.saveAll(usersCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        // Выполняем метод
        List<User> result = userService.signupUsers(dtoList);

        // Проверяем результаты
        assertEquals(1, result.size());
        assertEquals("new1", result.get(0).getPersonalId());

        // Проверяем сохраненных пользователей
        List<User> savedUsers = usersCaptor.getValue();
        assertEquals(1, savedUsers.size());
        assertEquals("John", savedUsers.get(0).getFirstName());
    }

    @Test
    void changeInf_UpdatesOnlyProvidedFields() {
        // Подготавливаем тестовые данные
        UserSetDTO dto = new UserSetDTO();
        dto.setFirstName("NewName");
        dto.setLastName("Doe"); // Обязательное поле
        dto.setPhoneNumber("+77071234567"); // Обязательное поле
        dto.setAddress("Astana"); // Обязательное поле
        dto.setPassword("newPass");

        User user = new User();
        user.setPersonalId("123");
        user.setFirstName("OldName");
        user.setLastName("Smith");
        user.setPhoneNumber("+77070000000");
        user.setAddress("Almaty");
        user.setPassword(PasswordUtil.hashPassword("oldPass"));

        when(userRepository.findByPersonalId("123")).thenReturn(user);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Выполняем метод
        UserGetDTO result = userService.changeInf(dto, "123");

        // Проверяем обновленные поля
        assertEquals("NewName", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("+77071234567", result.getPhoneNumber());
        assertEquals("Astana", result.getAddress());

        // Проверяем пароль
        assertTrue(PasswordUtil.checkPassword("newPass", user.getPassword()));

        // Проверяем вызов сохранения
        verify(userRepository).save(user);
    }

    @Test
    void signup_WithEmptyPersonalId_ThrowsInvalidPersonalIdException() {
        validSignupDTO.setPersonalId("");
        assertThrows(InvalidPersonalIdException.class, () -> userService.signup(validSignupDTO));
    }

    @Test
    void signup_WithWeakPassword_ThrowsWeakPasswordException() {
        validSignupDTO.setPassword("");
        assertThrows(WeakPasswordException.class, () -> userService.signup(validSignupDTO));
    }

    @Test
    void signup_WithInvalidPhone_ThrowsInvalidPhoneNumberException() {
        validSignupDTO.setPhoneNumber("123");
        assertThrows(InvalidPhoneNumberException.class, () -> userService.signup(validSignupDTO));
    }

    @Test
    void signup_WithUnexpectedError_ThrowsRuntimeException() {
        when(userRepository.existsByPersonalId(anyString())).thenThrow(new RuntimeException("DB error"));
        assertThrows(RuntimeException.class, () -> userService.signup(validSignupDTO));
    }

    @Test
    void login_WithNonExistingUser_ThrowsException() {
        when(userRepository.findByPersonalId("invalid")).thenReturn(null);
        LoginDTO loginDTO = new LoginDTO("invalid", "pass");

        assertThrows(NullPointerException.class, () -> userService.login(loginDTO, request));
    }


    @Test
    void addInvoice_WhenFacilityNotFound_ReturnsFalse() {
        when(facilityRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertFalse(userService.addInvoice(999L, "123"));
    }

    @Test
    void delete_WithMixedInvoices_HandlesAllTypes() {
        User user = new User();
        Invoice openInvoice = new Invoice(100.0, "open");
        Invoice closedInvoice = new Invoice(100.0, CLOSE);
        user.setInvoices(Arrays.asList(openInvoice, closedInvoice));

        when(userRepository.findByPersonalId("123")).thenReturn(user);

        userService.delete("123");

        verify(invoiceRepository, times(1)).delete(closedInvoice);
        verify(invoiceRepository, never()).delete(openInvoice);
    }

    @Test
    void getInvoicesByUserAndStatus_WhenNoInvoices_ReturnsEmptyList() {
        when(invoiceRepository.findByUserIdAndStatus(anyLong(), anyString()))
                .thenReturn(Collections.emptyList());

        List<InvoiceDTO> result = userService.getInvoicesByUserAndStatus(1L, "open");

        assertTrue(result.isEmpty());
    }

    @Test
    void addPayment_WithZeroAmount_ReturnsFalse() {
        AddPaymentDTO dto = new AddPaymentDTO();
        dto.setInvoiceId(1L);
        dto.setAmount(0.0);

        assertFalse(userService.addPayment(dto, "123"));
    }


    @Test
    void addPayment_FullPayment_ClosesInvoice() {
        // Arrange
        AddPaymentDTO dto = new AddPaymentDTO();
        dto.setInvoiceId(1L);
        dto.setAmount(100.0);

        User user = new User();
        user.setPersonalId("123");
        user.setBalance(200.0);

        Invoice invoice = new Invoice(100.0, "open");
        invoice.setRemainder(100.0);
        invoice.setUser(user);

        when(userRepository.findByPersonalId("123")).thenReturn(user);
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        when(paymentRepository.save(any(Payment.class))).thenReturn(new Payment());

        // Act
        boolean result = userService.addPayment(dto, "123");

        // Assert
        assertTrue(result);
        assertEquals(CLOSE, invoice.getStatus());
        assertEquals(0.0, invoice.getRemainder());
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void addPayment_WithClosedInvoice_ReturnsFalse() {
        // Arrange
        AddPaymentDTO dto = new AddPaymentDTO();
        dto.setInvoiceId(1L);
        dto.setAmount(50.0);

        User user = new User();
        user.setPersonalId("123");

        Invoice invoice = new Invoice(100.0, CLOSE);
        invoice.setUser(user);

        when(userRepository.findByPersonalId("123")).thenReturn(user);
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));

        // Act
        boolean result = userService.addPayment(dto, "123");

        // Assert
        assertFalse(result);
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void addInvoice_WhenCacheNotPresent_ShouldNotUpdateCache() {
        // Arrange
        User user = new User();
        user.setPersonalId("123");

        Facility facility = new Facility();
        facility.setId(1L);

        when(userRepository.findByPersonalId("123")).thenReturn(user);
        when(facilityRepository.findById(1L)).thenReturn(Optional.of(facility));
        when(invoiceGetDTOCache.isPresent(123L)).thenReturn(false);

        // Act
        boolean result = userService.addInvoice(1L, "123");

        // Assert
        assertTrue(result);
        verify(invoiceGetDTOCache, never()).putInCache(anyLong(), anyList());
    }
}
