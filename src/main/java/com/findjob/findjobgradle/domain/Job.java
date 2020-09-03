package com.findjob.findjobgradle.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Table(name = "JOB")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "CATEGORY")
    private Category category;

    @Column(name = "COMPANY")
    private String company;

    @Column(name = "CITY")
    private String city;

    @Column(name = "PUBLISHED")
    private boolean published;

    @Column(name = "user_fk")
    Long userId;

    @OneToMany(mappedBy = "jobId",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE,orphanRemoval = true)
    private Set<DocumentFile> files;

    @OneToOne(mappedBy = "job", cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY, optional = false)
    private JobDetails jobDetails;


    public Job() {
    }

    public Job(String title, Category category, String company, String city, boolean published) {
        this.title = title;
        this.category = category;
        this.company = company;
        this.city = city;
        this.published = published;
        this.files=new HashSet<>();
    }

    public void setJobDetails(JobDetails jobDetails) {
        if(jobDetails == null) {
            if(this.jobDetails != null) {
                this.jobDetails.setJob(null);
            }
        } else {
            jobDetails.setJob(this);
        }
        this.jobDetails = jobDetails;
    }
}
