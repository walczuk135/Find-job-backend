package com.findjob.findjobgradle.service;

import com.findjob.findjobgradle.controller.JobExceptionHandler;
import com.findjob.findjobgradle.controller.jobDto.JobDto;
import com.findjob.findjobgradle.controller.jobDto.JobPostDto;
import com.findjob.findjobgradle.controller.jobDto.MapperJobOfferDto;
import com.findjob.findjobgradle.domain.Category;
import com.findjob.findjobgradle.domain.Job;
import com.findjob.findjobgradle.domain.JobDetails;
import com.findjob.findjobgradle.domain.User;
import com.findjob.findjobgradle.domain.security.Role;
import com.findjob.findjobgradle.domain.security.RoleType;
import com.findjob.findjobgradle.repository.JobRepository;
import com.findjob.findjobgradle.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class JobServiceTest {

    @MockBean
    private JobRepository repository;

    @MockBean
    private UserRepository userRepository;


    @Autowired
    private JobService service;

    @Mock
    private Clock clock;

    static final Clock fixed = Clock.fixed(Instant.parse("2020-05-01T12:00:00.000Z"), ZoneId.systemDefault());
    static final LocalDateTime DATE_TIME = LocalDateTime.now(fixed);

    @BeforeEach
    void setUp() {
        service = new JobService(repository,userRepository);
    }

    @Test
    void testFindByIdSuccess() {
        //given
        Job job = new Job("Python Developer", Category.IT, "Lublin", "Lublin",true);
        JobDetails jobDetails = new JobDetails(LocalDateTime.now(), LocalDateTime.now(), "description", "example@kol.com");
        job.setJobDetails(jobDetails);
        job.setId(1L);

        //when
        when(repository.findById(1L)).thenReturn(Optional.of(job));
        Optional<Job> returnedJob = service.getJobOfferById(1L);

        //then
        verify(repository, times(1)).findById(1L);
        assertThat(returnedJob.isPresent()).isTrue();
        assertThat(returnedJob.get()).isEqualTo(job);
        assertThat(jobDetails).isEqualTo(returnedJob.get().getJobDetails());
    }

    @Test
    void testFindByIdNotFound() {
        //when
        Optional<Job> jobOfferById = service.getJobOfferById(1);
        //then
        verify(repository, times(1)).findById(1L);
        assertThat(jobOfferById.isPresent()).isEqualTo(false);
    }

    @Test
    void testGetSingleJobById() {
        //given
        Job job = new Job("Python Developer", Category.IT, "Billenium", "Lublin",true);
        JobDetails jobDetails=new JobDetails(LocalDateTime.now(), DATE_TIME, "dadada","ddadaadda");
        job.setJobDetails(jobDetails);

        //when
        when(repository.findById(1L)).thenReturn(Optional.of(job));
        JobDto singleJobById = service.getSingleJobById(1L).get();

        //then
        verify(repository, times(1)).findById(1L);
        assertThat(job.getId()).isEqualTo(singleJobById.getId());
        assertThat(job.getCategory()).isEqualTo(singleJobById.getCategory());
        assertThat(job.getTitle()).isEqualTo(singleJobById.getTitle());
        assertThat(job.getCompany()).isEqualTo(singleJobById.getCompany());
    }

    @Test
    void testGetSingleJobByIdNotFound() {
        //when
        Optional<JobDto> singleJobById = service.getSingleJobById(1);

        //then
        verify(repository, times(1)).findById(1L);
        assertThat(singleJobById.isPresent()).isEqualTo(false);
    }

    @Test
    void testFindAllFound() {
        //given
        Job job = new Job("Python Developer", Category.IT, "Lublin", "Lublin",true);
        JobDetails jobDetails = new JobDetails(LocalDateTime.now(), LocalDateTime.now(), "description", "example@kol.com");
        Job job1 = new Job("Python Developer", Category.IT, "Lublin", "Lublin",true);
        JobDetails jobDetails1 = new JobDetails(LocalDateTime.now(), LocalDateTime.now(), "description", "example@kol.com");
        Job job2 = new Job("Python Developer", Category.IT, "Lublin", "Lublin",true);
        JobDetails jobDetails2 = new JobDetails(LocalDateTime.now(), LocalDateTime.now(), "description", "example@kol.com");
        job.setJobDetails(jobDetails);
        job1.setJobDetails(jobDetails1);
        job2.setJobDetails(jobDetails2);

        List<Job> listMockJob = new ArrayList<>();
        listMockJob.add(job);
        listMockJob.add(job1);
        listMockJob.add(job2);

        //when
        when(repository.findAll()).thenReturn(listMockJob);
        List<Job> allJob = service.getALLJobOffer();

        //then
        verify(repository, times(1)).findAll();
        assertThat(allJob).isEqualTo(listMockJob);
        assertThat(allJob).hasSize(3).contains(job, job1, job2);
    }
//
//    @Test
//    void testSave() {
//        //given
//        Job job = new Job("Python Developer", Category.IT, "Lublin", "Lublin",true);
//        JobDetails jobDetails = new JobDetails(LocalDateTime.now(),
//                LocalDateTime.parse("2016-03-04 11:30:40", MapperJobOfferDto.formatter), "description", "example@kol.com");
//        job.setJobDetails(jobDetails);
//        job.setId(1L);
//        jobDetails.setId(1L);
//        JobPostDto jobPostDto = new JobPostDto("Python developer", "IT", "Britenet", "Lublin",
//                "dadadada", "2016-03-04 11:30:40", "example@kol.com",true);
//
//        Role role3 = new Role(RoleType.ROLE_ADMIN);
//        role3.setId(1L);
//        User user = new User("user135", "user@gmail.com", "12345678");
//        user.setId(1L);
//        user.setRoles(Set.of(role3));
//        job.setUserId(user.getId());
//        //when
//        when(userRepository.findByUsername("user135")).thenReturn(user);
//        when(userRepository.save(user)).thenReturn(user);
//        when(repository.save(job)).thenReturn(job);
//        when(repository.findById(1L)).thenReturn(Optional.of(job));
//
//        Job returnedJob = service.saveJobOffer(jobPostDto, "user135");
//        //then
//        assertThat(returnedJob).isNotNull();
//        assertThat(returnedJob).isEqualTo(job);
//        assertThat(returnedJob.getId()).isEqualTo(returnedJob.getId());
//        assertThat(jobDetails).isEqualTo(returnedJob.getJobDetails());
//    }

    @Test
    void testDelete() {
        //given
        Job job = new Job("Python Developer", Category.IT, "Lublin", "Lublin",true);
        JobDetails jobDetails = new JobDetails(LocalDateTime.now(),
                LocalDateTime.parse("2016-03-04 11:30:40", MapperJobOfferDto.formatter), "description", "example@kol.com");
        job.setJobDetails(jobDetails);
        job.setId(1L);
        Role role3 = new Role(RoleType.ROLE_ADMIN);
        role3.setId(1L);
        User user = new User("user135", "user@gmail.com", "12345678");
        user.setId(1L);
        user.setRoles(Set.of(role3));
        when(userRepository.findByUsername("user135")).thenReturn(user);
        when(repository.existsJobByIdAndUserId(1,1)).thenReturn(true);
        //when
        service.deleteJob(1,"user135");
        //then
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNotFound() {
        //given
        String exceptionMessage = "No job offers with this id and user name found !";

        //when
        willThrow(new JobExceptionHandler(exceptionMessage)).given(repository).deleteById(20L);
        JobExceptionHandler exception = assertThrows(
                JobExceptionHandler.class,
                () -> service.deleteJob(20,"user135")
        );

        //then
        assertThat(exceptionMessage).isEqualTo(exception.getMessage());
        assertThat(service.getALLJobOffer()).hasSize(0);
    }


    @Test
    void updateJobTest() {
        //given
        Job job = new Job("Python Developer", Category.IT, "Lublin", "Lublin",true);
        JobDetails jobDetails = new JobDetails(LocalDateTime.now(),
                LocalDateTime.parse("2016-03-04 11:30:40", MapperJobOfferDto.formatter), "description", "example@kol.com");
        job.setJobDetails(jobDetails);
        job.setId(1L);
        jobDetails.setId(1L);
        JobPostDto jobPostDto = new JobPostDto("Python developer", "IT", "Britenet", "Lublin",
                "dadadada", "2016-03-04 11:30:40", "example@kol.com",true);

        Role role3 = new Role(RoleType.ROLE_ADMIN);
        role3.setId(1L);
        User user = new User("user135", "user@gmail.com", "12345678");
        user.setId(1L);
        user.setRoles(Set.of(role3));
        job.setUserId(user.getId());

        //when
        when(repository.existsJobByIdAndUserId(1,user.getId())).thenReturn(true);
        when(repository.findById(1L)).thenReturn(Optional.of(job));
        when(userRepository.findByUsername("user135")).thenReturn(user);
        Optional<Job> returnedJob = service.update(jobPostDto,job.getId(),"user135");

        //then
        assertThat(returnedJob).isNotNull();
        assertThat(returnedJob.get().getTitle()).isEqualTo(jobPostDto.getTitle());
        assertThat(returnedJob.get()).isEqualTo(job);
        assertThat(returnedJob.get().getId()).isEqualTo(returnedJob.get().getId());
        assertThat(jobDetails).isEqualTo(returnedJob.get().getJobDetails());
    }


}
