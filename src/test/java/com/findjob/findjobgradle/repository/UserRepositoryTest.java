package com.findjob.findjobgradle.repository;

import com.findjob.findjobgradle.domain.Category;
import com.findjob.findjobgradle.domain.Job;
import com.findjob.findjobgradle.domain.JobDetails;
import com.findjob.findjobgradle.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(value = SpringExtension.class)
@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"})
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    @Test
    public void testFindByUserNameSuccess() {
        //given
        User user = new User("user135", "user@gmail.com", "12345678");
        //when
        entityManager.persistAndFlush(user);
        User userReturned = repository.findByUsername("user135");
        //then
        assertThat(userReturned).isNotNull();
        assertThat(userReturned.getUsername()).isEqualTo(user.getUsername());
        entityManager.clear();
    }

    @Test
    public void testFindByUserNameNotSuccess() {
        //given
        //when
        User userReturned = repository.findByUsername("user135");
        //then
        assertThat(userReturned).isNull();
    }

//    @Test
//    public void testFindAllUserJobOffer() {
//        //given
//        Job job = new Job("Python Developer", Category.IT, "Lublin", "Lublin", true);
//        JobDetails jobDetails = new JobDetails(LocalDateTime.now(), LocalDateTime.now(), "description", "example@kol.com");
//        Job job1 = new Job("Python Developer", Category.IT, "Lublin", "Lublin", true);
//        JobDetails jobDetails1 = new JobDetails(LocalDateTime.now(), LocalDateTime.now(), "description", "example@kol.com");
//        Job job2 = new Job("Python Developer", Category.IT, "Lublin", "Lublin", true);
//        JobDetails jobDetails2 = new JobDetails(LocalDateTime.now(), LocalDateTime.now(), "description", "example@kol.com");
//        job.setJobDetails(jobDetails);
//        job1.setJobDetails(jobDetails1);
//        job2.setJobDetails(jobDetails2);
//      //  job.setUserId(1L);
//     //   job1.setUserId(1L);
//     //   job2.setUserId(1L);
//
//        User user = new User("user135", "user@gmail.com", "12345678");
//
//        //when
//        entityManager.persistAndFlush(user);
//        user.getJobs().add(job);
//        user.getJobs().add(job1);
//        user.getJobs().add(job2);
//        entityManager.persistAndFlush(job);
//        entityManager.persistAndFlush(job1);
//        entityManager.persistAndFlush(job2);
//        List<Job> jobsUser = repository.findAllUserJobOffer("user135");
//        //then
//        assertThat(jobsUser).isNotNull();
//        assertThat(jobsUser).hasSize(3).contains(job, job1, job2);
//        entityManager.clear();
//    }

    @Test
    void testExistsByUsernameSuccess() {
        //given
        User user = new User("user135", "user@gmail.com", "12345678");
        //when
        entityManager.persistAndFlush(user);
        Boolean isExist = repository.existsByUsername("user135");
        //then
        assertThat(isExist).isNotNull();
        assertThat(isExist).isTrue();
        entityManager.clear();
    }

    @Test
    void testExistsByUsernameNotSuccess() {
        //given
        //when
        Boolean isExist = repository.existsByUsername("user135");
        //then
        assertThat(isExist).isNotNull();
        assertThat(isExist).isFalse();
    }

    @Test
    void testExistsByEmailSuccess() {
        //given
        User user = new User("user135", "user@gmail.com", "12345678");
        //when
        entityManager.persistAndFlush(user);
        Boolean isExist = repository.existsByEmail("user@gmail.com");
        //then
        assertThat(isExist).isNotNull();
        assertThat(isExist).isTrue();
        entityManager.clear();
    }

    @Test
    void testExistsByEmailNotSuccess() {
        //given
        //when
        Boolean isExist = repository.existsByUsername("user135");
        //then
        assertThat(isExist).isNotNull();
        assertThat(isExist).isFalse();
    }
}
