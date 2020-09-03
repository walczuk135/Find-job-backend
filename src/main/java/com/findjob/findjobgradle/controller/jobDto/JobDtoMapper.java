package com.findjob.findjobgradle.controller.jobDto;

import com.findjob.findjobgradle.domain.Job;

import java.util.List;
import java.util.stream.Collectors;

public class JobDtoMapper {

    private JobDtoMapper() {
    }

    public static JobDto mapToJobDto(Job job) {
        return new JobDto(job.getId(), job.getTitle(), job.getCategory(),
                job.getCompany(), job.getCity(),job.isPublished(),job.getJobDetails().getCreated().toString(),
                job.getJobDetails().getEndDated().toString(),job.getJobDetails().getDescription(),job.getJobDetails().getEmail());
    }

    public static List<JobDto> mapToJobsDto(List<Job> jobs) {
        return jobs.stream()
                .map(JobDtoMapper::mapToJobDto)
                .collect(Collectors.toList());
    }
}
