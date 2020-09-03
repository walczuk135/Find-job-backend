package com.findjob.findjobgradle.controller.jobDto;

import com.findjob.findjobgradle.domain.Category;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class JobDto {
    private Long id;
    private String title;
    private Category category;
    private String company;
    private String city;
    private boolean published;
    private String created;
    private String endDated;
    private String description;
    private String email;

    public JobDto(Long id, String title, Category category, String company, String city, boolean published, String created, String endDated, String description, String email) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.company = company;
        this.city = city;
        this.published = published;
        this.created = created;
        this.endDated = endDated;
        this.description = description;
        this.email = email;
    }
}
