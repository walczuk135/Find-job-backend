package com.findjob.findjobgradle.service;

import com.findjob.findjobgradle.controller.JobExceptionHandler;
import com.findjob.findjobgradle.controller.jobDto.JobDto;
import com.findjob.findjobgradle.controller.jobDto.JobDtoMapper;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class JobService {

    private static final int PAGE_SIZE = 10;

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public JobService(JobRepository jobRepository, UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    public Optional<Job> getJobOfferById(long id) {
        return jobRepository.findById(id);
    }

    public List<Job> getALLJobOffer() {
        return jobRepository.findAll();
    }

    public Optional<JobDto> getSingleJobById(long id) {
        return jobRepository.findById(id).map(JobDtoMapper::mapToJobDto);//.orElseThrow(() -> new JobExceptionHandler("No job offers with this id found !"))));
    }

    public List<JobDto> getALLSingleJobOffer(int page) {
        return JobDtoMapper.mapToJobsDto(jobRepository.findAllJobPageable(PageRequest.of(page, PAGE_SIZE)));
    }

    public List<JobDto> getJobsByCategoryAndCity(String category, String city, int page) {
        return JobDtoMapper.mapToJobsDto(jobRepository.findAllJobByCategoryAndCity(checkCategory(category), city, PageRequest.of(page, PAGE_SIZE)));
    }

    public List<JobDto> getJobsByCategory(String category, int page) {
        return JobDtoMapper.mapToJobsDto(jobRepository.findAllJobByCategory(checkCategory(category), PageRequest.of(page, PAGE_SIZE)));
    }

    public List<JobDto> getJobsByCity(String city, int page) {
        return JobDtoMapper.mapToJobsDto(jobRepository.findAllJobByCity(city, PageRequest.of(page, PAGE_SIZE)));
    }

    public List<JobDto> getJobsByTitle(String title, int page) {
        return JobDtoMapper.mapToJobsDto(jobRepository.findAllByTitleContainsIgnoreCase(title, PageRequest.of(page, PAGE_SIZE)));
    }

    public List<JobDto> getJobsByCompany(String company, int page) {
        return JobDtoMapper.mapToJobsDto(jobRepository.findAllJobByCompany(company, PageRequest.of(page, PAGE_SIZE)));
    }

    public List<JobDto> getJobsByPublished(boolean pub){
        return JobDtoMapper.mapToJobsDto(jobRepository.findByPublished(pub));
    }


    public boolean existJob(Long id) {
        return jobRepository.existsJobById(id);
    }

    @Transactional
    public Job saveJobOffer(JobPostDto jobPostDto, String username) {
        Job job = MapperJobOfferDto.mapToJob(jobPostDto);
        User byUsername = userRepository.findByUsername(username);
        JobDetails jobDetails = MapperJobOfferDto.mapToJobDetails(jobPostDto);
        jobDetails.setCreated(LocalDateTime.now());
        job.setJobDetails(jobDetails);
        job.setUserId(byUsername.getId());
        job.setPublished(true);
        Job save = jobRepository.save(job);

        byUsername.getJobs().add(save);

        return save;
    }

    @Transactional
    public boolean deleteJob(long id, String username) {
        try {
            User returnedUsername = userRepository.findByUsername(username);
            Set<Role> roles = returnedUsername.getRoles();

            if(roles.stream().anyMatch(r -> r.getName().equals(RoleType.ROLE_ADMIN))){
                jobRepository.deleteById(id);
                return true;
            }
            else if(jobRepository.existsJobByIdAndUserId(id, returnedUsername.getId())) {
                jobRepository.deleteById(id);
                return true;
            } else {
                throw new JobExceptionHandler("No job offers with this id and user name found !");
            }
        } catch(Exception e) {
            throw new JobExceptionHandler("No job offers with this id and user name found !");
        }
    }


    @Transactional
    public Optional<Job> update(JobPostDto jobPostDto, Long id, String username) {

        User returnedUsername = userRepository.findByUsername(username);
        Optional<Job> byId = jobRepository.findById(id);
        Set<Role> roles = returnedUsername.getRoles();

        if(isIfAdminOrMod(roles)) {
            return updateJob(jobPostDto, id, byId);
        } else if(jobRepository.existsJobByIdAndUserId(id, returnedUsername.getId())) {
            return updateJob(jobPostDto, id, byId);
        } else throw new JobExceptionHandler("Not authorize");
    }


    private Optional<Job> updateJob(JobPostDto jobPostDto, Long id, Optional<Job> byId) {
        return byId.map(job -> {
            Job job1 = MapperJobOfferDto.mapToJob(jobPostDto);
            JobDetails jobDetails = MapperJobOfferDto.mapToJobDetails(jobPostDto);

            job.setCategory(job1.getCategory());
            job.setCity(job1.getCity());
            job.setCompany(job1.getCompany());
            job.setTitle(job1.getTitle());

            job.getJobDetails().setCreated(jobDetails.getCreated());
            job.getJobDetails().setEndDated(jobDetails.getEndDated());
            job.getJobDetails().setDescription(jobDetails.getDescription());
            job.getJobDetails().setEmail(jobDetails.getEmail());
            job.getJobDetails().setJob(job);

            return job;
        });
    }

    private boolean isIfAdminOrMod(Set<Role> roles) {
        return roles.stream().anyMatch(r -> r.getName().equals(RoleType.ROLE_ADMIN) || r.getName().equals(RoleType.ROLE_MODERATOR));
    }

    private Category checkCategory(String category) {
        for(Category c : Category.values()) {
            if(c.name().equals(category)) {
                return Category.valueOf(category);
            }
        }
        return Category.OTHER;
    }
}
