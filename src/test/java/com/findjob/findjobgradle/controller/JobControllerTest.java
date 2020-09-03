package com.findjob.findjobgradle.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.findjob.findjobgradle.controller.jobDto.JobDto;
import com.findjob.findjobgradle.controller.jobDto.JobPostDto;
import com.findjob.findjobgradle.controller.payload.request.SignupRequest;
import com.findjob.findjobgradle.domain.Category;
import com.findjob.findjobgradle.domain.Job;
import com.findjob.findjobgradle.domain.JobDetails;
import com.findjob.findjobgradle.domain.User;
import com.findjob.findjobgradle.domain.security.Role;
import com.findjob.findjobgradle.domain.security.RoleType;
import com.findjob.findjobgradle.repository.JobRepository;
import com.findjob.findjobgradle.security.services.UserDetailsImpl;
import com.findjob.findjobgradle.service.JobService;
import com.findjob.findjobgradle.service.RegistrationService;
import com.findjob.findjobgradle.service.UserDetailsServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobService service;

    @MockBean
    private RegistrationService registrationService;

    @MockBean
    private JobRepository jobRepository;

    @MockBean
    UserDetailsServiceImpl userDetailsServiceIml;

    @MockBean
    Authentication authentication;

    static final Clock fixed = Clock.fixed(Instant.parse("2020-05-01T12:00:00.000Z"), ZoneId.systemDefault());
    static final LocalDateTime DATE_TIME = LocalDateTime.now(fixed);

    @Test
    public void testGetJobOfferAndDetailsIdFound() throws Exception {

        Job job = generateTestJob();

        given(service.getJobOfferById(1L)).willReturn(Optional.of(job));

        //when
        ResultActions result = mockMvc.perform(get("/api/jobs/1/details/"))
                .andExpect(status().isOk());

        //then
        result.andExpect(header().string(HttpHeaders.LOCATION, "/api/jobs/" + 1 + "/details/"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.category", is("IT")))
                .andExpect(jsonPath("$.company", is("Billenium")))
                .andExpect(jsonPath("$.city", is("Lublin")))
                .andExpect(jsonPath("$.title", is("Python Developer")))
                .andExpect(jsonPath("$.jobDetails.id", is(1)));
    }

    @Test
    public void testGetSingleJob() throws Exception {
        //given
        JobDto jobDto = new JobDto(1L, "Python Developer", Category.IT, "Billenium", "Lublin",
                true, LocalDateTime.now().toString(), DATE_TIME.toString(), "dadadad", "sfsfsfsfs");
        given(service.getSingleJobById(anyLong())).willReturn(Optional.of(jobDto));

        //when
        ResultActions result = mockMvc.perform(get("/api/jobs/1/"))
                .andExpect(status().isOk());

        //then
        result.andExpect(header().string(HttpHeaders.LOCATION, "/api/jobs/1/"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.category", is("IT")))
                .andExpect(jsonPath("$.company", is("Billenium")))
                .andExpect(jsonPath("$.city", is("Lublin")))
                .andExpect(jsonPath("$.title", is("Python Developer")));
    }

    @Test
    public void testGetAllJobFound() throws Exception {
        //given
        given(service.getALLSingleJobOffer(0)).willReturn(testListJob());

        //when
        ResultActions result = mockMvc.perform(get("/api/jobs/").param("page", "0"));

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
    public void testGetAllJobPageableFound() throws Exception {
        //given
        given(service.getALLSingleJobOffer(1)).willReturn(testListJob());

        //when
        ResultActions result = mockMvc.perform(get("/api/jobs/").param("page", "1"));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[11].id", is(12)))
                .andReturn();
    }

    @Test
    public void testGetJobOfferByIdNotFound() throws Exception {
        //given
        given(service.getJobOfferById(1000)).willReturn(Optional.empty());
        //when
        ResultActions result = mockMvc.perform(get("/api/jobs/{id}", 1000));
        //then
        result.andExpect(status().isNotFound());
    }

    @Test
    public void testCreateJobOfferNotAuthorize() throws Exception {
        //given
        Job job = generateTestJob();
        JobPostDto jobPostDto = new JobPostDto("Python developer", "IT", "Britenet", "Lublin",
                "dadadada", "2016-03-04 11:30:40", "example@kol.com", true);
        given(service.saveJobOffer(jobPostDto, "user135")).willReturn(job);

        //when
        ResultActions result = mockMvc.perform(post("/api/jobs/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(jobPostDto)));

        //then
        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void testCreateJobOffer() throws Exception {
        //given
        Job job = generateTestJob();

        JobPostDto jobPostDto = new JobPostDto("Python developer", "IT", "Britenet", "Lublin",
                "dadadada", "2016-03-04 11:30:40", "example@kol.com", true);

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

        given(service.saveJobOffer(any(JobPostDto.class), anyString())).willReturn(job);

        //when
        ResultActions result = mockMvc.perform(post("/api/jobs/")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + compact)
                .content(asJsonString(jobPostDto)));

        //then
        result.andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/jobs/1/"))
                .andExpect(jsonPath("$.title", is("Python Developer")))
                .andExpect(jsonPath("$.category", is("IT")));
    }

    @Test
    void testJobDeleteNoAuthorize() throws Exception {
        //given
        given(service.getJobOfferById(1)).willReturn(Optional.empty());

        //when
        ResultActions result = mockMvc.perform(delete("/api/jobs/{id}/", 1));

        //then
        result.andExpect(status().isUnauthorized());
    }

    @Test
    void testJobDeleteSuccess() throws Exception {
        //given
        Job job = generateTestJob();
        given(service.getJobOfferById(1)).willReturn(Optional.of(job));

        SignupRequest signupRequest = new SignupRequest("user135", "user135@gmail.com", Set.of("admin"), "12345678");
        Role role3 = new Role(RoleType.ROLE_ADMIN);
        role3.setId(1L);
        User user = new User("user135", "user@gmail.com", "12345678");
        user.setId(1L);
        user.setRoles(Set.of(role3));

        given(registrationService.registerUser(signupRequest)).willReturn(user);
        User user1 = registrationService.registerUser(signupRequest);

        given(userDetailsServiceIml.loadUserByUsername(user1.getUsername())).willReturn(UserDetailsImpl.builderUserDetails(user1));
        given(jobRepository.existsJobByIdAndUserId(1, 1)).willReturn(true);
        given(service.deleteJob(1, "user135")).willReturn(true);
        String compact = generateTestToken(user1);

        //when
        ResultActions result = mockMvc.perform(delete("/api/jobs/{id}/", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + compact)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk());
    }

    @Test
    void testJobDeleteNotYoursJob() throws Exception {
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
        given(jobRepository.existsJobByIdAndUserId(1, 1)).willReturn(false);
        given(service.deleteJob(1, "user135")).willReturn(false);
        String compact = generateTestToken(user1);

        //when
        ResultActions result = mockMvc.perform(delete("/api/jobs/{id}/", "2")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + compact)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isNotFound());
    }

    @Test
    void testJobDeleteNotFound() throws Exception {

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
        ResultActions result = mockMvc.perform(delete("/api/jobs/{id}/", "2")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + compact)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isNotFound());
    }

    @Test
    public void shouldUpdateJobTest() throws Exception {
        //given
        Job job = new Job("Java Developer", Category.IT, "Billenium", "Lublin", true);
        JobDetails jobDetails = new JobDetails(LocalDateTime.now(), DATE_TIME, "Awesome", "billemium@gmial.com");
        job.setJobDetails(jobDetails);
        job.setId(1L);
        jobDetails.setId(1L);
        given(service.getJobOfferById(1)).willReturn(Optional.of(job));

        JobPostDto jobPostDto = new JobPostDto("Python developer", "IT", "Britenet", "Lublin",
                "dadadada", "2016-03-04 11:30:40", "example@kol.com", true);

        Job job1 = new Job("Python developer", Category.IT, "Britenet", "Lublin", true);
        JobDetails jobDetails1 = new JobDetails(LocalDateTime.now(), DATE_TIME, "dadadada", "example@kol.com");
        job1.setJobDetails(jobDetails);
        job1.setId(1L);
        jobDetails1.setId(1L);

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

        given(service.existJob(1L)).willReturn(true);
        given(service.update(jobPostDto, 1L, user.getUsername())).willReturn(Optional.of(job1));
        //when
        ResultActions result = mockMvc.perform(put("/api/jobs/1/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + compact)
                .content(asJsonString(jobPostDto)));
        //then
        result.andExpect(status().isNoContent());
    }


    private String generateTestToken(User user1) {
        return Jwts.builder()
                .setSubject((user1.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 1800000))
                .signWith(SignatureAlgorithm.HS512, "addddddddddddddddafmo304o2o4-o2-4krfls")
                .compact();
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Job generateTestJob() {
        Job job = new Job("Python Developer", Category.IT, "Billenium", "Lublin", true);
        JobDetails jobDetails = new JobDetails(LocalDateTime.now(), DATE_TIME, "Awesome", "billemium@gmial.com");
        job.setJobDetails(jobDetails);
        job.setId(1L);
        jobDetails.setId(1L);
        return job;
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
}
