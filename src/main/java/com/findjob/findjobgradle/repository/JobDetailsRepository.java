package com.findjob.findjobgradle.repository;

import com.findjob.findjobgradle.domain.JobDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobDetailsRepository extends JpaRepository<JobDetails,Long> {
}
