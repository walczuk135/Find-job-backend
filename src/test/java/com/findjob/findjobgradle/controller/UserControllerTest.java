package com.findjob.findjobgradle.controller;

import com.findjob.findjobgradle.controller.jobDto.JobDto;
import com.findjob.findjobgradle.controller.payload.request.SignupRequest;
import com.findjob.findjobgradle.domain.Category;
import com.findjob.findjobgradle.domain.User;
import com.findjob.findjobgradle.domain.security.Role;
import com.findjob.findjobgradle.domain.security.RoleType;
import com.findjob.findjobgradle.security.services.UserDetailsImpl;
import com.findjob.findjobgradle.service.RegistrationService;
import com.findjob.findjobgradle.service.UserDetailsServiceImpl;
import com.findjob.findjobgradle.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistrationService registrationService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserDetailsServiceImpl userDetailsServiceIml;

    static final Clock fixed = Clock.fixed(Instant.parse("2020-05-01T12:00:00.000Z"), ZoneId.systemDefault());
    static final LocalDateTime DATE_TIME = LocalDateTime.now(fixed);


    @Test
    public void testShouldGetUserInfoNotAuthorize() throws Exception {
        //given
        given(userService.getInfo("user135")).willReturn(null);

        //when
        ResultActions result = mockMvc.perform(get("/api/user/")
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void testShouldGetUserInfoSuccess() throws Exception {
        //given
        SignupRequest signupRequest = new SignupRequest("user135", "user135@gmail.com", Set.of("user", "mod", "admin"), "12345678");
        Role role3 = new Role(RoleType.ROLE_ADMIN);
        role3.setId(1L);
        User user = new User("user135", "user@gmail.com", "12345678");
        user.setId(1L);
        user.setRoles(Set.of(role3));

        given(registrationService.registerUser(signupRequest)).willReturn(user);
        User user1 = registrationService.registerUser(signupRequest);
        given(userDetailsServiceIml.loadUserByUsername(user1.getUsername())).willReturn(UserDetailsImpl.builderUserDetails(user1));

        UserDetailsImpl build = UserDetailsImpl.builderUserDetails(user1);
        String compact = generateTestToken(user1);
        given(userService.getInfo(user.getUsername())).willReturn(build);

        //when
        ResultActions result = mockMvc.perform(get("/api/user/")
                .header("Authorization", "Bearer " + compact)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is(build.getUsername())))
                .andExpect(jsonPath("$.email", is(build.getEmail())));
    }

    @Test
    public void shouldGetUserJobOfferNotAuthorize() throws Exception {
        //when
        ResultActions result = mockMvc.perform(get("/api/user/jobs/")
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldGetUserJobOfferSuccess() throws Exception {
        //given
        SignupRequest signupRequest = new SignupRequest("user135", "user135@gmail.com", Set.of("user", "mod", "admin"), "12345678");
        Role role3 = new Role(RoleType.ROLE_ADMIN);
        role3.setId(1L);
        User user = new User("user135", "user@gmail.com", "12345678");
        user.setId(1L);
        user.setRoles(Set.of(role3));

        given(registrationService.registerUser(signupRequest)).willReturn(user);
        User user1 = registrationService.registerUser(signupRequest);
        given(userDetailsServiceIml.loadUserByUsername(user1.getUsername())).willReturn(UserDetailsImpl.builderUserDetails(user1));

        String compact = generateTestToken(user1);

        given(userService.getYoursJobOffer(user1.getUsername())).willReturn(testListJob());

        //when
        ResultActions result = mockMvc.perform(get("/api/user/jobs/")
                .header("Authorization", "Bearer " + compact)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].category", is("IT")))
                .andExpect(jsonPath("$[0].company", is("Billenium")))
                .andExpect(jsonPath("$[0].city", is("Lublin")))
                .andExpect(jsonPath("$[0].title", is("Python Developer")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[3].id", is(4)))
                .andExpect(jsonPath("$[4].id", is(5)))
                .andExpect(jsonPath("$[5].id", is(6)))
                .andExpect(jsonPath("$[6].id", is(7)))
                .andExpect(jsonPath("$[7].id", is(8)))
                .andExpect(jsonPath("$[8].id", is(9)))
                .andExpect(jsonPath("$[9].id", is(10)))
                .andExpect(jsonPath("$[10].id", is(11)))
                .andExpect(jsonPath("$[11].id", is(12)))
                .andReturn();
    }

    @Test
    public void shouldDeleteYourAccountNoAuthorise() throws Exception {
        //when
        ResultActions result = mockMvc.perform(delete("/api/user")
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldDeleteYourAccountSuccess() throws Exception {
        //given
        SignupRequest signupRequest = new SignupRequest("user135", "user135@gmail.com", Set.of("user", "mod", "admin"), "12345678");
        Role role3 = new Role(RoleType.ROLE_ADMIN);
        role3.setId(1L);
        User user = new User("user135", "user@gmail.com", "12345678");
        user.setId(1L);
        user.setRoles(Set.of(role3));

        given(registrationService.registerUser(signupRequest)).willReturn(user);
        User user1 = registrationService.registerUser(signupRequest);
        given(userDetailsServiceIml.loadUserByUsername(user1.getUsername())).willReturn(UserDetailsImpl.builderUserDetails(user1));

        String compact = generateTestToken(user1);

        //when
        ResultActions result = mockMvc.perform(delete("/api/user/")
                .header("Authorization", "Bearer " + compact)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isNoContent());
    }

    @Test
    public void shouldDeleteAccountNotAuthorize() throws Exception {
        //when
        ResultActions result = mockMvc.perform(delete("/api/user","user135")
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isUnauthorized());
    }


    @Test
    public void shouldDeleteAccountUser() throws Exception {
        //given
        SignupRequest signupRequest = new SignupRequest("user135", "user135@gmail.com", Set.of("admin"), "12345678");
        Role role3 = new Role(RoleType.ROLE_ADMIN);
        role3.setId(1L);
        User user = new User("user135", "user@gmail.com", "12345678");
        user.setId(1L);
        user.setRoles(Set.of(role3));

        given(registrationService.registerUser(signupRequest)).willReturn(user);
        User user1 = registrationService.registerUser(signupRequest);
        given(userDetailsServiceIml.loadUserByUsername(user1.getUsername())).willReturn(UserDetailsImpl.builderUserDetails(user1));
        String compact = generateTestToken(user1);

        //when
        ResultActions result = mockMvc.perform(delete("/api/user/","user135")
                .header("Authorization", "Bearer " + compact)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isNoContent());
    }

    private List<JobDto> testListJob() {
        return List.of(new JobDto(1L, "Python Developer", Category.IT, "Billenium", "Lublin",
                        true, LocalDateTime.now().toString(), DATE_TIME.toString(), "dadadad", "sfsfsfsfs"),

                new JobDto(2L, "Python Developer", Category.IT, "Billenium", "Lublin",
                        true, LocalDateTime.now().toString(), DATE_TIME.toString(), "dadadad", "sfsfsfsfs"),

                new JobDto(3L, "Python Developer", Category.IT, "Billenium", "Lublin",
                        true, LocalDateTime.now().toString(), DATE_TIME.toString(), "dadadad", "sfsfsfsfs"),

                new JobDto(4L, "Python Developer", Category.IT, "Billenium", "Lublin",
                        true, LocalDateTime.now().toString(), DATE_TIME.toString(), "dadadad", "sfsfsfsfs"),

                new JobDto(5L, "Python Developer", Category.IT, "Billenium", "Lublin",
                        true, LocalDateTime.now().toString(), DATE_TIME.toString(), "dadadad", "sfsfsfsfs"),

                new JobDto(6L, "Python Developer", Category.IT, "Billenium", "Lublin",
                        true, LocalDateTime.now().toString(), DATE_TIME.toString(), "dadadad", "sfsfsfsfs"),

                new JobDto(7L, "Python Developer", Category.IT, "Billenium", "Lublin",
                        true, LocalDateTime.now().toString(), DATE_TIME.toString(), "dadadad", "sfsfsfsfs"),

                new JobDto(8L, "Python Developer", Category.IT, "Billenium", "Lublin",
                        true, LocalDateTime.now().toString(), DATE_TIME.toString(), "dadadad", "sfsfsfsfs"),

                new JobDto(9L, "Python Developer", Category.IT, "Billenium", "Lublin",
                        true, LocalDateTime.now().toString(), DATE_TIME.toString(), "dadadad", "sfsfsfsfs"),

                new JobDto(10L, "Python Developer", Category.IT, "Billenium", "Lublin",
                        true, LocalDateTime.now().toString(), DATE_TIME.toString(), "dadadad", "sfsfsfsfs"),

                new JobDto(11L, "Python Developer", Category.IT, "Billenium", "Lublin",
                        true, LocalDateTime.now().toString(), DATE_TIME.toString(), "dadadad", "sfsfsfsfs"),

                new JobDto(12L, "Python Developer", Category.IT, "Billenium", "Lublin",
                        true, LocalDateTime.now().toString(), DATE_TIME.toString(), "dadadad", "sfsfsfsfs"));
    }

    private String generateTestToken(User user1) {
        return Jwts.builder()
                .setSubject((user1.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 1800000))
                .signWith(SignatureAlgorithm.HS512, "addddddddddddddddafmo304o2o4-o2-4krfls")
                .compact();
    }
}
