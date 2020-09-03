package com.findjob.findjobgradle.repository;

import com.findjob.findjobgradle.domain.DocumentFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentFileRepository  extends JpaRepository<DocumentFile, Long> {
    List<DocumentFile> findAllByJobId(Long id);
}
