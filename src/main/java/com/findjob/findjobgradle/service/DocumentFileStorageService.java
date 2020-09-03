package com.findjob.findjobgradle.service;

import com.findjob.findjobgradle.controller.JobExceptionHandler;
import com.findjob.findjobgradle.domain.DocumentFile;
import com.findjob.findjobgradle.domain.Job;
import com.findjob.findjobgradle.domain.User;
import com.findjob.findjobgradle.repository.DocumentFileRepository;
import com.findjob.findjobgradle.repository.JobRepository;
import com.findjob.findjobgradle.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@Getter
@Setter
public class DocumentFileStorageService {

    private final DocumentFileRepository documentFileRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;


    public DocumentFileStorageService(DocumentFileRepository documentFileRepository, JobRepository jobRepository, UserRepository userRepository) {
        this.documentFileRepository = documentFileRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public boolean store(Long id, MultipartFile file) throws IOException {

        if (jobRepository.existsJobById(id)) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

            Optional<Job> byId = jobRepository.findById(id);

            DocumentFile documentFile = new DocumentFile(fileName, file.getContentType(), file.getBytes());

            documentFile.setJobId(id);
            byId.map(offer -> {
                offer.getFiles().add(documentFile);
                return offer;
            });

            documentFileRepository.save(documentFile);
            return true;
        } else {
            return false;
        }

    }

    public Optional<DocumentFile> getDocumentFile(Long id) {
        return documentFileRepository.findById(id);
    }

    @Transactional
    public List<DocumentFile> getAllDocumentFiles(Long idJob, String name) {

        if (jobRepository.existsJobById(idJob)) {
            User byUsername = userRepository.findByUsername(name);
            if (jobRepository.existsJobByIdAndUserId(idJob, byUsername.getId())) {
              return documentFileRepository.findAllByJobId(idJob);
            } else throw new JobExceptionHandler("This is not yours jobs documents");
        } else throw new JobExceptionHandler("This job not exist");

    }

}
