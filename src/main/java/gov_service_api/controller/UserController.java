package gov_service_api.controller;

import gov_service_api.dto.user.*;
import gov_service_api.model.*;
import gov_service_api.service.*;
import gov_service_api.dto.*;
import jakarta.servlet.http.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final String NO_LOGIN = "user not logged in";
    private static final String PERSONAL_ID = "personalId";

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<StringDTO> signup(@RequestBody SignupDTO signupDTO) {

        if (userService.signup(signupDTO)) {
            return ResponseEntity.ok(new StringDTO("User registered successfully"));
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new StringDTO("Username already exists"));
    }

    @PostMapping("/login")
    public ResponseEntity<StringDTO> login(
            @RequestBody LoginDTO loginDto, HttpServletRequest request) {

        boolean authenticated = userService.login(loginDto, request);

        if (authenticated) {
            return ResponseEntity.ok(new StringDTO("User Login Success"));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new StringDTO("User Login Failed"));
    }

    @GetMapping("/getProfile")
    public ResponseEntity<UserGetDTO> getProfile(HttpServletRequest request) {

        String personalId = (String) request.getSession().getAttribute(PERSONAL_ID);

        if (personalId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.ok(userService.getProfile(personalId));
    }

    @PostMapping("/addInvoice")
    public ResponseEntity<StringDTO> addInvoice(
            @RequestBody LongDTO facilityId, HttpServletRequest request) {

        String personalId = (String) request.getSession().getAttribute(PERSONAL_ID);

        if (personalId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new StringDTO(
                    NO_LOGIN
            ));
        }

        if (userService.addInvoice(facilityId.getId(), personalId)) {
            return ResponseEntity.ok(new StringDTO("Invoice added successfully"));
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(new StringDTO(
                "facility don't exist"
        ));
    }

    @GetMapping("/getInvoices")
    public ResponseEntity<List<InvoiceDTO>> getInvoices(HttpServletRequest request) {

        String personalId = (String) request.getSession().getAttribute(PERSONAL_ID);

        if (personalId != null) {
            return ResponseEntity.ok(userService.getInvoices(personalId));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @GetMapping("/invoices")
    public ResponseEntity<List<InvoiceDTO>> getInvoices(
            @RequestParam Long userId,
            @RequestParam String status) {
        return ResponseEntity.ok(userService.getInvoicesByUserAndStatus(userId, status));
    }

    @PostMapping("/addPayment")
    public ResponseEntity<StringDTO> addPayment(@RequestBody AddPaymentDTO addPaymentDTO,
                                                HttpServletRequest request) {

        String personalId = (String) request.getSession().getAttribute(PERSONAL_ID);

        if (personalId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new StringDTO(
                    NO_LOGIN
            ));
        }

        if (userService.addPayment(addPaymentDTO, personalId)) {
            return ResponseEntity.ok(new StringDTO("Payment added successfully"));
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(new StringDTO(
                "invoice don't exist or money don't enough"
        ));
    }

    @PatchMapping("/changeInf")
    public ResponseEntity<UserGetDTO> changeInf(
            @RequestBody UserSetDTO userSetDTO, HttpServletRequest request) {

        String personalId = (String) request.getSession().getAttribute(PERSONAL_ID);

        if (personalId != null) {
            return ResponseEntity.ok(userService.changeInf(userSetDTO, personalId));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<StringDTO> delete(HttpServletRequest request) {

        String personalId = (String) request.getSession().getAttribute(PERSONAL_ID);

        if (personalId != null) {
            userService.delete(personalId);
            return ResponseEntity.ok(new StringDTO("User deleted successfully"));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new StringDTO(NO_LOGIN));
    }
}
