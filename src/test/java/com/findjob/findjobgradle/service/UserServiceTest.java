package com.findjob.findjobgradle.service;

import com.findjob.findjobgradle.controller.jobDto.JobDto;
import com.findjob.findjobgradle.controller.jobDto.MapperJobOfferDto;
import com.findjob.findjobgradle.domain.Category;
import com.findjob.findjobgradle.domain.Job;
import com.findjob.findjobgradle.domain.JobDetails;
import com.findjob.findjobgradle.domain.User;
import com.findjob.findjobgradle.domain.security.Role;
import com.findjob.findjobgradle.domain.security.RoleType;
import com.findjob.findjobgradle.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserServiceTest {

    @MockBean
    private UserRepository userRepository;
   
    @Autowired
    private UserService userService;

    @Test
    public void findUserByNameSuccessTest() throws UserExceptionHandler {
        //given
        Role role3 = new Role(RoleType.ROLE_ADMIN);
        role3.setId(1L);
        User user = new User("user135", "user@gmail.com", "12345678");
        user.setId(1L);
        user.setRoles(Set.of(role3));
        //when
        when(userRepository.findByUsername("user135")).thenReturn(user);
        Optional<User> returnedUser = userService.findUserByName("user135");
        //then
        verify(userRepository, times(1)).findByUsername("user135");
        assertThat(returnedUser.isPresent()).isTrue();
        assertThat(returnedUser.get()).isEqualTo(user);
    }

    @Test
    public void findUserByNameNotSuccessTest() throws UserExceptionHandler {
        //given
        String exceptionMessage = "Not find user this name.";
        //when
        willThrow(new UserExceptionHandler(exceptionMessage)).given(userRepository).findByUsername("adadadaaddaaddaad");
        UserExceptionHandler exception = assertThrows(
                UserExceptionHandler.class,
                () -> userService.findUserByName("adadadaaddaaddaad")
        );
        //then
        //verify(userRepository, times(0)).findByUsername("adadadaaddaaddaad");
        assertThat(exceptionMessage).isEqualTo(exception.getMessage());
    }

    @Test
    public void existsByUsernameSuccessTest(){
        //given
        String username="user135";
        //when
        when(userRepository.existsByUsername(username)).thenReturn(true);
        boolean exist = userService.existsByUsername(username);
        //then
        assertThat(exist).isTrue();
        verify(userRepository, times(1)).existsByUsername(username);
    }

    @Test
    public void existsByUsernameNotSuccessTest(){
        //given
        String username="user135";
        //when
        when(userRepository.existsByUsername(username)).thenReturn(false);
        boolean exist = userService.existsByUsername(username);
        //then
        assertThat(exist).isFalse();
        verify(userRepository, times(1)).existsByUsername(username);
    }

    @Test
    public void existsByEmailSuccessTest(){
        //given
        String email="user135@gmail.com";
        //when
        when(userRepository.existsByEmail(email)).thenReturn(true);
        boolean exist = userService.existsByEmail(email);
        //then
        assertThat(exist).isTrue();
        verify(userRepository, times(1)).existsByEmail(email);
    }

    @Test
    public void existsByEmailNotSuccessTest(){
        //given
        String email="user135@gmail.com";
        //when
        when(userRepository.existsByEmail(email)).thenReturn(false);
        boolean exist = userService.existsByEmail(email);
        //then
        assertThat(exist).isFalse();
        verify(userRepository, times(1)).existsByEmail(email);
    }

    @Test
    public void getInfoTest(){
        //given
        String username="user135";
        Role role3 = new Role(RoleType.ROLE_ADMIN);
        role3.setId(1L);
        User user = new User("user135", "user@gmail.com", "12345678");
        user.setId(1L);
        user.setRoles(Set.of(role3));
        //when
        when(userRepository.findByUsername(username)).thenReturn(user);
        UserDetails info = userService.getInfo(username);
        //then
        assertThat(info).isNotNull();
        assertThat(info.getUsername()).isEqualTo("user135");
    }

    @Test
    public void getYoursJobOffer(){
        //given
        String username="user135";
        Job job = new Job("Python Developer", Category.IT, "Britenet", "Lublin",true);
        JobDetails jobDetails = new JobDetails(LocalDateTime.now(),
                LocalDateTime.parse("2016-03-04 11:30:40", MapperJobOfferDto.formatter), "description", "example@kol.com");
        job.setJobDetails(jobDetails);
        job.setId(1L);
        jobDetails.setId(1L);
        Job job2 = new Job("Python Developer", Category.IT, "Britenet", "Lublin",true);
        JobDetails jobDetails2 = new JobDetails(LocalDateTime.now(),
                LocalDateTime.parse("2016-03-04 11:30:40", MapperJobOfferDto.formatter), "description", "example@kol.com");
        job.setJobDetails(jobDetails);
        job2.setJobDetails(jobDetails2);
        job.setId(2L);
        jobDetails.setId(2L);
        //when
        when(userRepository.findAllUserJobOffer(username)).thenReturn(List.of(job,job2));
        List<JobDto> userListJob = userService.getYoursJobOffer(username);
        //then
        assertThat(userListJob).hasSize(2);
    }

    @Test
    public void saveUserTest(){
        //given
        Role role3 = new Role(RoleType.ROLE_ADMIN);
        role3.setId(1L);
        User user = new User("user135", "user@gmail.com", "12345678");
        user.setId(1L);
        user.setRoles(Set.of(role3));
        //when
        when(userRepository.save(user)).thenReturn(user);
        User user1 = userService.saveUser(user);
        //then
        assertThat(user1).isEqualTo(user);
    }

    @Test
    public void addJobToUser(){
        //given
        Role role3 = new Role(RoleType.ROLE_ADMIN);
        role3.setId(1L);
        User user = new User("user135", "user@gmail.com", "12345678");
        user.setId(1L);
        user.setRoles(Set.of(role3));
        Job job = new Job("Python Developer", Category.IT, "Britenet", "Lublin",true);
        JobDetails jobDetails = new JobDetails(LocalDateTime.now(),
                LocalDateTime.parse("2016-03-04 11:30:40", MapperJobOfferDto.formatter), "description", "example@kol.com");
        job.setJobDetails(jobDetails);
        job.setId(1L);
        jobDetails.setId(1L);
        //when
        when(userRepository.findByUsername("user135")).thenReturn(user);
        when(userRepository.findAllUserJobOffer("user135")).thenReturn(List.of(job));

        Job job1 = userService.addJobToUser("user135", job);
        //then
        assertThat(job1).isEqualTo(job);
    }

    @Test
    public void deleteMyAccountTest(){
        //given
        Role role3 = new Role(RoleType.ROLE_ADMIN);
        role3.setId(1L);
        User user = new User("user135", "user@gmail.com", "12345678");
        user.setId(1L);
        user.setRoles(Set.of(role3));
        when(userRepository.findByUsername("user135")).thenReturn(user);
        //when
        userService.deleteMyAccount("user135");
        //then
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void deleteAccountSuccessTest(){
        //given
        Role role3 = new Role(RoleType.ROLE_ADMIN);
        role3.setId(1L);
        User user = new User("user135", "user@gmail.com", "12345678");
        user.setId(1L);
        user.setRoles(Set.of(role3));
        when(userRepository.findByUsername("user135")).thenReturn(user);
        when(userRepository.existsByUsername("user135")).thenReturn(true);
        //when
        userService.deleteUser("user135");
        //then
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void deleteAccountNotSuccessTest(){
        //given
        String exceptionMessage="There is no user with username";
        //when
        when(userRepository.existsByUsername("user135")).thenReturn(false);
        UserExceptionHandler exception = assertThrows(
                UserExceptionHandler.class,
                () -> userService.deleteUser("user135")
        );
        //then
        verify(userRepository, times(0)).deleteById(1L);
        assertThat(exception.getMessage()).isEqualTo(exceptionMessage);
    }
}
