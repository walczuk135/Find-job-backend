package com.findjob.findjobgradle.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@Entity
@EqualsAndHashCode(of = "id")
@Table(name = "FILES")
public class DocumentFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    private String name;

    private String type;

    @Column(name = "job_fk")
    Long jobId;

    @Lob
    private byte[] data;

    public DocumentFile() {
    }

    public DocumentFile(String name, String type, byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }

}
