package com.findjob.findjobgradle.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.findjob.findjobgradle.controller.payload.request.LoginRequest;
import com.findjob.findjobgradle.controller.payload.request.SignupRequest;
import com.findjob.findjobgradle.controller.payload.response.JwtResponse;
import com.findjob.findjobgradle.domain.User;
import com.findjob.findjobgradle.domain.security.Role;
import com.findjob.findjobgradle.domain.security.RoleType;
import com.findjob.findjobgradle.security.services.UserDetailsImpl;
import com.findjob.findjobgradle.service.LoginService;
import com.findjob.findjobgradle.service.RegistrationService;
import com.findjob.findjobgradle.service.UserDetailsServiceImpl;
import com.findjob.findjobgradle.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RegistrationService registrationService;
    @MockBean
    private LoginService loginService;
    @MockBean
    private UserDetailsServiceImpl userDetailsServiceIml;
    @MockBean
    private UserService userService;

    @Test
    public void authenticateUserTest() throws Exception {
        //given
        LoginRequest loginRequest=new LoginRequest();
        loginRequest.setUsername("user135");
        loginRequest.setPassword("12345678");
        JwtResponse jwtResponse=new JwtResponse("asasaas",1L,"user135","walczuk135@gmail.com", List.of("admin"));
        given(loginService.authenticateUser(loginRequest)).willReturn(jwtResponse);
        //when
        ResultActions result = mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(loginRequest)));
        //then
        result.andExpect(status().isOk());
    }

    @Test
    public void registerUserTest() throws Exception {
        //given
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setRole(Set.of("admin"));
        signupRequest.setUsername("user135");
        signupRequest.setEmail("user135@gmail.com");
        signupRequest.setPassword("12345678");

        Role role3 = new Role(RoleType.ROLE_ADMIN);
        role3.setId(1L);
        User user = new User("user135", "user@gmail.com", "12345678");
        user.setId(1L);
        user.setRoles(Set.of(role3));

        given(registrationService.registerUser(signupRequest)).willReturn(user);
        User user1 = registrationService.registerUser(signupRequest);
        given(userDetailsServiceIml.loadUserByUsername(user1.getUsername())).willReturn(UserDetailsImpl.builderUserDetails(user1));
        given(registrationService.registerUser(signupRequest)).willReturn(user1);
        //when
        ResultActions result = mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(signupRequest)));
        //then
        result.andExpect(status().isCreated());
    }

    @Test
    public void registerUserNotSuccess() throws Exception {
        //given
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setRole(Set.of("admin"));
        signupRequest.setUsername("user135");
        signupRequest.setEmail("user135@gmail.com");
        signupRequest.setPassword("12345678");

        Role role3 = new Role(RoleType.ROLE_ADMIN);
        role3.setId(1L);
        User user = new User("user135", "user@gmail.com", "12345678");
        user.setId(1L);
        user.setRoles(Set.of(role3));

        given(registrationService.registerUser(signupRequest)).willReturn(user);
        User user1 = registrationService.registerUser(signupRequest);
        given(userDetailsServiceIml.loadUserByUsername(user1.getUsername())).willReturn(UserDetailsImpl.builderUserDetails(user1));
        given(registrationService.registerUser(signupRequest)).willReturn(user1);
        given(registrationService.registerUser(signupRequest)).willReturn(user1);
        //when
        ResultActions result = mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString("dadadadadadaad")));
        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void registerUserNotSuccessBadRequestUsernameIsAlreadyTaken() throws Exception {
        //given
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setRole(Set.of("admin"));
        signupRequest.setUsername("user135");
        signupRequest.setEmail("user135@gmail.com");
        signupRequest.setPassword("12345678");

        Role role3 = new Role(RoleType.ROLE_ADMIN);
        role3.setId(1L);
        User user = new User("user135", "user135@gmail.com", "12345678");
        user.setId(1L);
        user.setRoles(Set.of(role3));

        given(registrationService.registerUser(signupRequest)).willReturn(user);
        User user1 = registrationService.registerUser(signupRequest);
        given(userDetailsServiceIml.loadUserByUsername(user1.getUsername())).willReturn(UserDetailsImpl.builderUserDetails(user1));
        given(userService.existsByUsername(signupRequest.getUsername())).willReturn(true);

        //when
        ResultActions result = mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(signupRequest)));
        //then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Error: Username is already taken!")));
    }

    @Test
    public void registerUserNotSuccessBadRequestEmailIsAlreadyTaken() throws Exception {
        //given
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setRole(Set.of("admin"));
        signupRequest.setUsername("user135");
        signupRequest.setEmail("user135@gmail.com");
        signupRequest.setPassword("12345678");

        Role role3 = new Role(RoleType.ROLE_ADMIN);
        role3.setId(1L);
        User user = new User("user135", "user135@gmail.com", "12345678");
        user.setId(1L);
        user.setRoles(Set.of(role3));

        given(registrationService.registerUser(signupRequest)).willReturn(user);
        User user1 = registrationService.registerUser(signupRequest);
        given(userDetailsServiceIml.loadUserByUsername(user1.getUsername())).willReturn(UserDetailsImpl.builderUserDetails(user1));
        given(userService.existsByEmail(signupRequest.getEmail())).willReturn(true);

        //when
        ResultActions result = mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(signupRequest)));
        //then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Error: Email is already in use!")));
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
