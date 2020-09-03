package com.findjob.findjobgradle.service;

import com.findjob.findjobgradle.controller.payload.request.LoginRequest;
import com.findjob.findjobgradle.controller.payload.response.JwtResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class LoginServiceTest {

    @MockBean
    private LoginService loginService;

    @Test
    public void shouldAuthenticateUser(){
        //given
        LoginRequest loginRequest=new LoginRequest();
        loginRequest.setUsername("user135");
        loginRequest.setPassword("12345678");
        JwtResponse jwtResponse=new JwtResponse("asasaas",1L,"user135","walczuk135@gmail.com", List.of("admin"));
        //when
        when(loginService.authenticateUser(loginRequest)).thenReturn(jwtResponse);
        JwtResponse jwtResponse1 = loginService.authenticateUser(loginRequest);
        //then
        verify(loginService, times(1)).authenticateUser(loginRequest);
        assertThat(jwtResponse1).isEqualTo(jwtResponse);
    }

    @Test
    public void shouldnNotAuthenticateUser(){
        //given
        LoginRequest loginRequest=new LoginRequest();
        loginRequest.setUsername("user135");
        loginRequest.setPassword("12345678");
        //when
        when(loginService.authenticateUser(loginRequest)).thenReturn(null);
        JwtResponse jwtResponse1 = loginService.authenticateUser(loginRequest);
        //then
        verify(loginService, times(1)).authenticateUser(loginRequest);
        assertThat(jwtResponse1).isEqualTo(null);

    }
}
