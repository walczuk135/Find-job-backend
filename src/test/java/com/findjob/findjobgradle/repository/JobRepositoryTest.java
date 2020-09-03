package com.findjob.findjobgradle.repository;

import com.findjob.findjobgradle.domain.Category;
import com.findjob.findjobgradle.domain.Job;
import com.findjob.findjobgradle.domain.JobDetails;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(value = SpringExtension.class)
@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"})
class JobRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JobRepository repository;


    static final Clock fixed = Clock.fixed(Instant.parse("2020-05-01T12:00:00.000Z"), ZoneId.systemDefault());
    static final LocalDateTime DATE_TIME = LocalDateTime.now(fixed);

    @Test
    public void testFindByIdSuccess() {
        //given
        Job job = new Job("Python Developer", Category.IT, "Lublin", "Lublin",true);
        JobDetails jobDetails = new JobDetails(LocalDateTime.now(), LocalDateTime.now(), "description","example@kol.com");
        Job job1 = new Job("Python Developer", Category.IT, "Lublin", "Lublin",true);
        JobDetails jobDetails1 = new JobDetails(LocalDateTime.now(), LocalDateTime.now(), "description","example@kol.com");
        Job job2 = new Job("Python Developer", Category.IT, "Lublin", "Lublin",true);
        JobDetails jobDetails2 = new JobDetails(LocalDateTime.now(), LocalDateTime.now(), "description","example@kol.com");
        job.setJobDetails(jobDetails);
        job1.setJobDetails(jobDetails1);
        job2.setJobDetails(jobDetails2);

        //when
        entityManager.persistAndFlush(job);
        entityManager.persistAndFlush(job1);
        entityManager.persistAndFlush(job2);
        Optional<Job> byId = repository.findById(1L);

        assertThat(job.getTitle()).isEqualTo(byId.get().getTitle());
        assertThat(job.getId()).isEqualTo(job.getId());
        assertThat(job.getJobDetails().getId()).isEqualTo(job.getJobDetails().getId());
        entityManager.remove(job);

    }


    @Test
    void testFindByIdNotFound() {
        Job job = new Job("Python Developer", Category.IT, "Lublin", "Lublin",true);
        JobDetails jobDetails = new JobDetails(LocalDateTime.now(), LocalDateTime.now(), "description","example@kol.com");
        Job job1 = new Job("Python Developer", Category.IT, "Lublin", "Lublin",true);
        JobDetails jobDetails1 = new JobDetails(LocalDateTime.now(), LocalDateTime.now(), "description","example@kol.com");
        Job job2 = new Job("Python Developer", Category.IT, "Lublin", "Lublin",true);
        JobDetails jobDetails2 = new JobDetails(LocalDateTime.now(), LocalDateTime.now(), "description","example@kol.com");
        job.setJobDetails(jobDetails);
        job1.setJobDetails(jobDetails1);
        job2.setJobDetails(jobDetails2);

        entityManager.persistAndFlush(job);
        entityManager.persistAndFlush(job1);

        Optional<Job> jobOffer = repository.findById(3L);

        Assertions.assertFalse(jobOffer.isPresent());
    }

    @Test
    public void testFindAllJobSuccess() {
        Job job = new Job("Python Developer", Category.IT, "Lublin", "Lublin",true);
        JobDetails jobDetails = new JobDetails(LocalDateTime.now(), LocalDateTime.now(), "description","example@kol.com");
        Job job1 = new Job("Python Developer", Category.IT, "Lublin", "Lublin",true);
        JobDetails jobDetails1 = new JobDetails(LocalDateTime.now(), LocalDateTime.now(), "description","example@kol.com");
        Job job2 = new Job("Python Developer", Category.IT, "Lublin", "Lublin",true);
        JobDetails jobDetails2 = new JobDetails(LocalDateTime.now(), LocalDateTime.now(), "description","example@kol.com");
        job.setJobDetails(jobDetails);
        job1.setJobDetails(jobDetails1);
        job2.setJobDetails(jobDetails2);

        entityManager.persistAndFlush(job);
        entityManager.persistAndFlush(job1);
        entityManager.persistAndFlush(job2);

        List<Job> all = repository.findAll();

        assertThat(all).hasSize(3).contains(job,job1,job2);
    }

    @Test
    void testSave() {
        Job job = new Job("Python Developer", Category.IT, "Lublin", "Lublin",true);
        JobDetails jobDetails = new JobDetails(LocalDateTime.now(), LocalDateTime.now(), "description","example@kol.com");
        Job job1 = new Job("Python Developer", Category.IT, "Lublin", "Lublin",true);
        JobDetails jobDetails1 = new JobDetails(LocalDateTime.now(), LocalDateTime.now(), "description","example@kol.com");
        Job job2 = new Job("Python Developer", Category.IT, "Lublin", "Lublin",true);
        JobDetails jobDetails2 = new JobDetails(LocalDateTime.now(), LocalDateTime.now(), "description","example@kol.com");
        job.setJobDetails(jobDetails);
        job1.setJobDetails(jobDetails1);
        job2.setJobDetails(jobDetails2);

        repository.save(job);
        repository.save(job1);
        repository.save(job2);

        List<Job> all = repository.findAll();

        assertThat(all).hasSize(3).contains(job,job1,job2);
    }

    @Test
    void testDeleteById(){
        Job job = new Job("Python Developer", Category.IT, "Lublin", "Lublin",true);
        JobDetails jobDetails = new JobDetails(LocalDateTime.now(), LocalDateTime.now(), "description","example@kol.com");
        Job job1 = new Job("Python Developer", Category.IT, "Lublin", "Lublin",true);
        JobDetails jobDetails1 = new JobDetails(LocalDateTime.now(), LocalDateTime.now(), "description","example@kol.com");
        Job job2 = new Job("Python Developer", Category.IT, "Lublin", "Lublin",true);
        JobDetails jobDetails2 = new JobDetails(LocalDateTime.now(), LocalDateTime.now(), "description","example@kol.com");
        job.setJobDetails(jobDetails);
        job1.setJobDetails(jobDetails1);
        job2.setJobDetails(jobDetails2);

        entityManager.persistAndFlush(job);
        entityManager.persistAndFlush(job1);
        entityManager.persistAndFlush(job2);

        repository.deleteById(job.getId());

        List<Job> jobs=repository.findAll();

        assertThat(jobs).hasSize(2).contains(job1,job2);
    }

    @Test
    void testDeleteByIdNotFound(){
        assertThrows(EmptyResultDataAccessException.class, () -> repository.deleteById(1L));
    }


}
