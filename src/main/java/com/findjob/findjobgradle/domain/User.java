package com.findjob.findjobgradle.domain;

import com.findjob.findjobgradle.domain.security.Role;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@EqualsAndHashCode(of = "id")
@Table(name = "USER_",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "USERNAME"),
                @UniqueConstraint(columnNames = "EMAIL")
        })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 120)
    @Email
    private String email;

    @NotBlank
    @Size(max=120)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "USER_ROLES",
                joinColumns = @JoinColumn(name = "USER_ID"),
                inverseJoinColumns = @JoinColumn(name = "ROLES_ID"))
    private Set<Role> roles=new HashSet<>();


    @OneToMany(mappedBy = "userId",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE,orphanRemoval = true)
    private Set<Job> jobs;

    public User(String username,String email,String password) {
        this.username=username;
        this.email=email;
        this.password=password;
        this.jobs = new HashSet<>();
    }

    public User(){

    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", jobs=" + jobs +
                '}';
    }

    public  Job addJob(Job job){
        jobs.add(job);
        return job;
    }
}
