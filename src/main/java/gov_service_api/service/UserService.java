package gov_service_api.service;

import gov_service_api.dto.user.*;
import gov_service_api.model.*;
import gov_service_api.repository.*;
import gov_service_api.repository.cache.*;
import gov_service_api.security.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private UserRepository userRepository;
    private FacilityRepository facilityRepository;
    private InvoiceRepository invoiceRepository;
    private PaymentRepository paymentRepository;
    private UserDtoCache userDtoCache;

    @Autowired
    public UserService(UserRepository userRepository,
                       FacilityRepository facilityRepository,
                       InvoiceRepository invoiceRepository,
                       PaymentRepository paymentRepository,
                       UserDtoCache userDtoCache) {
        this.userRepository = userRepository;
        this.facilityRepository = facilityRepository;
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
        this.userDtoCache = userDtoCache;
    }

    public boolean signup(SignupDTO signupDTO) {

        boolean ans = userRepository.existsByPersonalId(signupDTO.getPersonalId());

        if (!ans) {

            String hashedPassword = PasswordUtil.hashPassword(signupDTO.getPassword());

            User user = new User(
                    signupDTO.getPersonalId(),
                    signupDTO.getFirstName(),
                    signupDTO.getLastName(),
                    signupDTO.getPhoneNumber(),
                    signupDTO.getAddress(),
                    hashedPassword
            );

            userRepository.save(user);

            return true;
        }

        return false;
    }

    public boolean login(LoginDTO loginDto, HttpServletRequest request) {

        User user = userRepository.findByPersonalId(loginDto.getPersonalId());

        boolean ans = PasswordUtil.checkPassword(loginDto.getPassword(), user.getPassword());

        System.out.println(ans);

        if (ans) {

            HttpSession session = request.getSession();

            session.setAttribute("personalId", loginDto.getPersonalId());

            return true;
        }

        return false;
    }

    public UserGetDTO getProfile(String personalId) {

        if (userDtoCache.isPresent(personalId)) {
            System.out.println("User already in cache table");
            return userDtoCache.getFromCache(personalId);
        }

        System.out.println("User not in cache table yet");
        User user = userRepository.findByPersonalId(personalId);

        userDtoCache.putInCache(personalId, new UserGetDTO(user));

        return new UserGetDTO(user);
    }

    public boolean addInvoice(Long facilityId, String personalId) {

        User user = userRepository.findByPersonalId(personalId);

        Optional<Facility> facilityOptional = facilityRepository.findById(facilityId);

        if (facilityOptional.isPresent()) {

            Facility facility = facilityOptional.get();

            Invoice invoice = new Invoice(
                    facility.getPrice(),
                    "open"
            );

            invoice.setFacility(facility);
            invoice.setUser(user);
            invoiceRepository.save(invoice);

            List<Invoice> invoices = user.getInvoices();
            invoices.add(invoice);
            user.setInvoices(invoices);
            userRepository.save(user);
            
            return true;
        }

        return false;
    }

    public List<InvoiceDTO> getInvoices(String personalId) {

        User user = userRepository.findByPersonalId(personalId);

        List<InvoiceDTO> invoicesDTO = new ArrayList<>();

        for (Invoice invoice : user.getInvoices()) {
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

    public boolean addPayment(AddPaymentDTO addPaymentDTO, String personalId) {

        User user = userRepository.findByPersonalId(personalId);

        Optional<Invoice> invoiceOptional = invoiceRepository.findById(
                addPaymentDTO.getInvoiceId());

        if (invoiceOptional.isPresent()
                && /*user.getBalance() >= addPaymentDTO.getAmount() &&*/
                addPaymentDTO.getAmount() != 0) {

            Invoice invoice = invoiceOptional.get();

            if (invoice.getStatus().equals("close")) {
                return false;
            }

            Payment payment = new Payment(
                    addPaymentDTO.getAmount()
            );

            payment.setInvoice(invoice);
            paymentRepository.save(payment);

            user.setBalance(user.getBalance() - addPaymentDTO.getAmount());
            userRepository.save(user);

            List<Payment> payments = invoice.getPayments();
            payments.add(payment);
            invoice.setRemainder(invoice.getRemainder() - addPaymentDTO.getAmount());
            if(invoice.getRemainder()==0.0) {
                invoice.setStatus("close");
            }
            invoice.setPayments(payments);
            invoiceRepository.save(invoice);

            return true;
        }

        return false;
    }

    public UserGetDTO changeInf(UserSetDTO userSetDTO, String personalId) {

        User user = userRepository.findByPersonalId(personalId);

        UserGetDTO userGetDTO = new UserGetDTO();
        userGetDTO.setPersonalId(personalId);
        userGetDTO.setBalance(user.getBalance());

        if (!userSetDTO.getFirstName().isEmpty()) {
            user.setFirstName(userSetDTO.getFirstName());
            userGetDTO.setFirstName(userSetDTO.getFirstName());
        }

        if (!userSetDTO.getLastName().isEmpty()) {
            user.setLastName(userSetDTO.getLastName());
            userGetDTO.setLastName(userSetDTO.getLastName());
        }

        if (!userSetDTO.getPhoneNumber().isEmpty()) {
            user.setPhoneNumber(userSetDTO.getPhoneNumber());
            userGetDTO.setPhoneNumber(userSetDTO.getPhoneNumber());
        }

        if (!userSetDTO.getAddress().isEmpty()) {
            user.setAddress(userSetDTO.getAddress());
            userGetDTO.setAddress(userSetDTO.getAddress());
        }

        if (!userSetDTO.getPassword().isEmpty()) {
            user.setPassword(PasswordUtil.hashPassword(userSetDTO.getPassword()));
        }

        userRepository.save(user);

        return userGetDTO;
    }

    public void delete(String personalId) {

        User user = userRepository.findByPersonalId(personalId);

        List<Invoice> invoices = user.getInvoices();

        for (Invoice invoice : invoices) {
            if (invoice.getStatus().equals("close")) {
                invoice.setFacility(null);
                invoice.setUser(null);
                invoiceRepository.save(invoice);
                invoiceRepository.delete(invoice);
            } else {
                invoice.setUser(null);
                invoiceRepository.save(invoice);
            }
        }

        user.setInvoices(null);
        userRepository.save(user);
        userRepository.delete(user);
    }

    public List<InvoiceDTO> getInvoicesByUserAndStatus(Long userId, String status) {

        List<Invoice> invoices = invoiceRepository.findByUserIdAndStatus(
                userId, status); // а это не работает

        List<InvoiceDTO> invoicesDTO = new ArrayList<>();

        for (Invoice invoice : invoices) {
            invoicesDTO.add(new InvoiceDTO(invoice));
        }

        return invoicesDTO;
    }
}
