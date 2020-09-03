package com.findjob.findjobgradle.controller.jobDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobPostDto {
    private String title;
    private String category;
    private String company;
    private String city;
    private String description;
    private String endDated;
    private String email;
    private boolean published;

    public JobPostDto(String title, String category, String company, String city, String description, String endDated, String email, boolean published) {
        this.title = title;
        this.category = category;
        this.company = company;
        this.city = city;
        this.description = description;
        this.endDated = endDated;
        this.email = email;
        this.published = published;
    }

    public JobPostDto() {

    }
}
