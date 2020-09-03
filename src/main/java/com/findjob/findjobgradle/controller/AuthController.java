package com.findjob.findjobgradle.controller;

import com.findjob.findjobgradle.controller.payload.request.LoginRequest;
import com.findjob.findjobgradle.controller.payload.request.SignupRequest;
import com.findjob.findjobgradle.controller.payload.response.MessageResponse;
import com.findjob.findjobgradle.service.LoginService;
import com.findjob.findjobgradle.service.RegistrationService;
import com.findjob.findjobgradle.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final RegistrationService registrationService;
    private final LoginService loginService;
    private final UserService userService;

    public AuthController(RegistrationService registrationService, LoginService loginService, UserService userService) {
        this.registrationService = registrationService;
        this.loginService = loginService;
        this.userService = userService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(loginService.authenticateUser(loginRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws URISyntaxException {
        if(userService.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if(userService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        registrationService.registerUser(signUpRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
