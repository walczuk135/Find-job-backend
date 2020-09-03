package com.findjob.findjobgradle.service;

import com.findjob.findjobgradle.controller.payload.request.SignupRequest;
import com.findjob.findjobgradle.domain.User;
import com.findjob.findjobgradle.domain.security.Role;
import com.findjob.findjobgradle.domain.security.RoleType;
import com.findjob.findjobgradle.repository.RoleRepository;
import com.findjob.findjobgradle.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class RegistrationServiceTest {

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private  RoleRepository roleRepository;
    @MockBean
    private  PasswordEncoder encoder;
    @Autowired
    private RegistrationService registrationService;

    @Test
    public void registrationUserSuccessTest() {
        //given
        SignupRequest signupRequest=new SignupRequest();
        signupRequest.setEmail("user135@gmail.com");
        signupRequest.setPassword("12345678");
        signupRequest.setUsername("user135");
        signupRequest.setRole(Set.of("admin"));
        Role role3 = new Role(RoleType.ROLE_ADMIN);
        role3.setId(1L);
        User user = new User(signupRequest.getUsername(),
                signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword()));
        user.setRoles(Set.of(role3));
        //when
        when(roleRepository.findByName(RoleType.ROLE_ADMIN)).thenReturn(Optional.of(role3));
        when(userRepository.save(user)).thenReturn(user);
        User user1 = registrationService.registerUser(signupRequest);
        //then
        assertThat(user1).isEqualTo(user);
        assertThat(user1.getEmail()).isEqualTo(user.getEmail());
        assertThat(user1.getJobs()).isEqualTo(user.getJobs());
    }

    @Test
    public void registrationUserNotSuccessInvalidRoleTest() {
        //given
        SignupRequest signupRequest=new SignupRequest();
        signupRequest.setEmail("user135@gmail.com");
        signupRequest.setPassword("12345678");
        signupRequest.setUsername("user135");
        signupRequest.setRole(Set.of("administrator"));

        //when
        RoleExceptionHandler exception = assertThrows(
                RoleExceptionHandler.class,
                () -> registrationService.registerUser(signupRequest)
        );

        //then
        assertThat("Error: Role is not found.").isEqualTo(exception.getMessage());

    }

}
