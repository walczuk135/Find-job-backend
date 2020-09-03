package com.findjob.findjobgradle.service;

import com.findjob.findjobgradle.controller.jobDto.JobDto;
import com.findjob.findjobgradle.controller.jobDto.JobDtoMapper;
import com.findjob.findjobgradle.domain.Job;
import com.findjob.findjobgradle.domain.User;
import com.findjob.findjobgradle.repository.JobRepository;
import com.findjob.findjobgradle.repository.UserRepository;
import com.findjob.findjobgradle.security.services.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;


    public UserService(UserRepository userRepository, JobRepository jobRepository) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    public Optional<User> findUserByName(String name)  {
        return Optional.ofNullable(userRepository.findByUsername(name));
    }

    public boolean existsByUsername(String name){
        return userRepository.existsByUsername(name);
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public UserDetails getInfo(String username){
        return UserDetailsImpl.builderUserDetails(userRepository.findByUsername(username));
    }

    public List<JobDto> getYoursJobOffer(String username) {
        return JobDtoMapper.mapToJobsDto(userRepository.findAllUserJobOffer(username));
    }

    @Transactional
    public User saveUser(User user){
        return userRepository.save(user);
    }

    @Transactional
    public Job addJobToUser(String user,Job job){
        User byUsername = userRepository.findByUsername(user);
         byUsername.getJobs().add(job);
        return job;
    }

    @Transactional
    public void deleteMyAccount(String username){
        deleteAllUserJobs(username);
    }

    @Transactional
    public void deleteUser(String username) {
        boolean isExist = userRepository.existsByUsername(username);
        if(isExist) {
            deleteAllUserJobs(username);
        } else throw new UserExceptionHandler("There is no user with username");
    }

    private void deleteAllUserJobs(String username) {
        User byId = userRepository.findByUsername(username);
        userRepository.delete(byId);
        List<Job> allUserJobOffer = userRepository.findAllUserJobOffer(byId.getUsername());
        for(Job job : allUserJobOffer) {
            jobRepository.delete(job);
        }
    }

    @Transactional
    public void deleteUserJobById(String userName,Job job){
        userRepository.findAllUserJobOffer(userName).remove(job);
    }
}
