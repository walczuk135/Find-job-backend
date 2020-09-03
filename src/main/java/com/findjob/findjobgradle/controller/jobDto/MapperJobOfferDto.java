package com.findjob.findjobgradle.controller.jobDto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.findjob.findjobgradle.domain.Category;
import com.findjob.findjobgradle.domain.Job;
import com.findjob.findjobgradle.domain.JobDetails;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MapperJobOfferDto {

    private ObjectMapper objectMapper;
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private MapperJobOfferDto() {
    }


    public static Job mapToJob(JobPostDto post) {
       return new Job(post.getTitle(),Category.valueOf(post.getCategory()),post.getCompany(),post.getCity(),post.isPublished());
    }

    public static JobDetails mapToJobDetails(JobPostDto post) {
        return new JobDetails(LocalDateTime.now(),LocalDateTime.parse(post.getEndDated(),formatter),post.getDescription(),post.getEmail());
    }

    public static JobPostDto mapToJobOfferPostDto(Job job){
        JobPostDto jobPostDto =new JobPostDto();
        jobPostDto.setCategory(job.getCategory().toString());
        jobPostDto.setCity(job.getCity());
        jobPostDto.setCompany(job.getCity());
        jobPostDto.setTitle(job.getTitle());
        jobPostDto.setEndDated(job.getJobDetails().getEndDated().toString());
        jobPostDto.setDescription(job.getJobDetails().getDescription());
        return jobPostDto;
    }

}
