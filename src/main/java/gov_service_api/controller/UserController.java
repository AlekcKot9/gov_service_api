package gov_service_api.controller;

import gov_service_api.dto.user.*;
import gov_service_api.model.*;
import gov_service_api.service.*;
import gov_service_api.dto.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.*;
import jakarta.servlet.http.*;
import jakarta.validation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user")
@Tag(name = "Пользователь", description = "Эндпоинты для работы с пользователем")
public class UserController {

    private static final String NO_LOGIN = "user not logged in";
    private static final String PERSONAL_ID = "personalId";

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Зарегестрировать")
    @PostMapping("/signup")
    public ResponseEntity<StringDTO> signup(@Valid @RequestBody SignupDTO signupDTO) {

        if (userService.signup(signupDTO)) {
            return ResponseEntity.ok(new StringDTO("User registered successfully"));
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new StringDTO("Username already exists"));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<User>> createUsersBulk(@RequestBody @Valid List<SignupDTO> users) {
        List<User> createdUsers = userService.signupUsers(users);
        return ResponseEntity.ok(createdUsers);
    }

    @Operation(summary = "Вход в аккаунт")
    @PostMapping("/login")
    public ResponseEntity<StringDTO> login(
            @Valid @RequestBody LoginDTO loginDto, HttpServletRequest request) {

        boolean authenticated = userService.login(loginDto, request);

        if (authenticated) {
            return ResponseEntity.ok(new StringDTO("User Login Success"));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new StringDTO("User Login Failed"));
    }

    @Operation(summary = "Выход из аккаунта")
    @GetMapping("/logout")
    public ResponseEntity<StringDTO> logout(HttpServletRequest request) {

        userService.logout(request);

        return ResponseEntity.ok(new StringDTO("User Logout Success"));
    }

    @Operation(summary = "Получить провиль пользователя")
    @GetMapping("/getProfile")
    public ResponseEntity<UserGetDTO> getProfile(HttpServletRequest request) {

        String personalId = (String) request.getSession().getAttribute(PERSONAL_ID);

        if (personalId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.ok(userService.getProfile(personalId));
    }

    @Operation(summary = "Добавить счет пользователю")
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

    @Operation(summary = "Получить все счета пользлвателя")
    @GetMapping("/getInvoices")
    public ResponseEntity<List<InvoiceDTO>> getInvoices(HttpServletRequest request) {

        String personalId = (String) request.getSession().getAttribute(PERSONAL_ID);

        if (personalId != null) {
            return ResponseEntity.ok(userService.getInvoices(Long.parseLong(personalId)));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @Operation(summary = "Получить получить все счета пользователя по id")
    @GetMapping("/invoices")
    public ResponseEntity<List<InvoiceDTO>> getInvoices(
            @RequestParam Long userId,
            @RequestParam String status) {
        return ResponseEntity.ok(userService.getInvoicesByUserAndStatus(userId, status));
    }

    @Operation(summary = "Получить только открытые счета")
    @GetMapping("/getWithOpenInv")
    public ResponseEntity<List<UserGetDTO>> getWithOpenInv(@RequestParam String status) {
        return ResponseEntity.ok(userService.getWithOpenInv(status));
    }

    @Operation(summary = "Добавить оплату")
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

    @Operation(summary = "Изменения информации пользователя")
    @PatchMapping("/changeInf")
    public ResponseEntity<UserGetDTO> changeInf(
            @Valid @RequestBody UserSetDTO userSetDTO, HttpServletRequest request) {

        String personalId = (String) request.getSession().getAttribute(PERSONAL_ID);

        if (personalId != null) {
            return ResponseEntity.ok(userService.changeInf(userSetDTO, personalId));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @Operation(summary = "Удалить аккаунт")
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
