package com.findjob.findjobgradle.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = "id")
@Getter
@Setter
@Table(name = "JOB_DETAILS")
public class JobDetails {
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "CREATED")
    private LocalDateTime created;

    @Column(name = "END_DATED")
    private LocalDateTime endDated;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "EMAIL")
    private String email;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Job job;

    public JobDetails() {
    }

    public JobDetails(LocalDateTime created, LocalDateTime endDated, String description,String email) {
        this.created = created;
        this.endDated = endDated;
        this.description = description;
        this.email=email;
    }

}
