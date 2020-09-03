package com.findjob.findjobgradle.controller;

import com.findjob.findjobgradle.controller.jobDto.JobDto;
import com.findjob.findjobgradle.controller.jobDto.JobPostDto;
import com.findjob.findjobgradle.domain.Job;
import com.findjob.findjobgradle.service.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
//@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobOfferService;

    public JobController(JobService jobOfferService) {
        this.jobOfferService = jobOfferService;
    }

    @GetMapping("/api/jobs/{id}/details/")
    public ResponseEntity<?> getJobOfferByIdWitchDetails(@PathVariable("id") Long id) {
        return jobOfferService.getJobOfferById(id)
                .map(offer -> ResponseEntity
                        .ok()
                        .location(URI.create("/api/jobs/" + offer.getId() + "/details/"))
                        .body(offer))
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }

    @GetMapping("/api/jobs")
    public Iterable<JobDto> getJobs(@RequestParam(required = false) Integer page) {
        int pageNumber = page != null && page >= 0 ? page : 0;
        return jobOfferService.getALLSingleJobOffer(pageNumber);
    }

    @GetMapping("/api/jobs/category/")
    public List<JobDto> getJobOfferByCategory(@RequestParam("category") String category, @RequestParam(required = false) Integer page) {
        int pageNumber = page != null && page >= 0 ? page : 0;
        return jobOfferService.getJobsByCategory(category, pageNumber);
    }

    @GetMapping("/api/jobs/city/")
    public List<JobDto> getJobOfferByCity(@RequestParam("city") String city, @RequestParam(required = false) Integer page) {
        int pageNumber = page != null && page >= 0 ? page : 0;
        return jobOfferService.getJobsByCity(city, pageNumber);
    }

    @GetMapping("/api/jobs/title")
    public List<JobDto> getJobOfferByTitle(@RequestParam(required = false) String title, @RequestParam(required = false) Integer page) {
        int pageNumber = page != null && page >= 0 ? page : 0;
        return jobOfferService.getJobsByTitle(title, pageNumber);
    }

    @GetMapping("/api/jobs/company/")
    public List<JobDto> getJobOfferByCompany(@RequestParam("company") String company, @RequestParam(required = false) Integer page) {
        int pageNumber = page != null && page >= 0 ? page : 0;
        return jobOfferService.getJobsByCompany(company, pageNumber);
    }

    @GetMapping("/api/jobs/category?city/")
    public List<JobDto> getJobOfferByCompany(@RequestParam("category") String category, @RequestParam("city") String city, @RequestParam(required = false) Integer page) {
        int pageNumber = page != null && page >= 0 ? page : 0;
        return jobOfferService.getJobsByCategoryAndCity(category, city, pageNumber);
    }

    @GetMapping("/api/jobs/published/")
    public ResponseEntity<List<JobDto>> getByPublished() {
        try {
            List<JobDto> jobs = jobOfferService.getJobsByPublished(true);

            if(jobs.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(jobs, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/jobs/{id}")
    public ResponseEntity<?> getSingleJob(@PathVariable("id") Long id) {
        return jobOfferService.getSingleJobById(id)
                .map(offer -> ResponseEntity
                        .ok()
                        .location(URI.create("/api/jobs/" + offer.getId()+"/"))
                        .body(offer))
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }

    @PostMapping("/api/jobs/")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Job> createJobs(@RequestBody JobPostDto postDto, Authentication authentication) {
            Job newJob = jobOfferService.saveJobOffer(postDto, authentication.getName());
            return ResponseEntity
                    .created(URI.create("/api/jobs/" + newJob.getId() + "/"))
                    .body(newJob);
    }

    @DeleteMapping("/api/jobs/{id}/")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteJob(@PathVariable Long id, Authentication authentication) {
        try {
            if(jobOfferService.deleteJob(id, authentication.getName())) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch(JobExceptionHandler e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/api/jobs/{id}/")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateJob(@PathVariable("id") Long id, @RequestBody JobPostDto jobPostDto, Authentication authentication) throws URISyntaxException {
        if(jobOfferService.existJob(id)) {
            try {
                jobOfferService.update(jobPostDto, id, authentication.getName());
                return ResponseEntity.noContent().build();
            } catch(JobExceptionHandler e) {
                return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.UNAUTHORIZED);
            }
        } else {
            Job job = jobOfferService.saveJobOffer(jobPostDto, authentication.getName());
            return ResponseEntity.created(new URI("/jobs/" + job.getId() + "/")).build();
        }
    }


}
