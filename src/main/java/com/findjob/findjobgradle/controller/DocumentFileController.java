package com.findjob.findjobgradle.controller;

import com.findjob.findjobgradle.controller.payload.response.ResponseFile;
import com.findjob.findjobgradle.controller.payload.response.ResponseMessage;
import com.findjob.findjobgradle.domain.DocumentFile;
import com.findjob.findjobgradle.service.DocumentFileStorageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController()
public class DocumentFileController {

    private final DocumentFileStorageService service;

    public DocumentFileController(DocumentFileStorageService service) {
        this.service = service;
    }

    @PostMapping("/api/jobs/{id}/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@PathVariable long id, @RequestParam("file") MultipartFile file) throws IOException {
        String message = "";

        if (service.store(id, file)) {
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } else {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/api/jobs/{id}/files")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<ResponseFile>> getListFiles(@PathVariable long id, Authentication authentication) {
        try {
            List<DocumentFile> allDocumentFiles = service.getAllDocumentFiles(id, authentication.getName());
            List<ResponseFile> files = allDocumentFiles.stream().map(file -> {
                String fileDownloadUri = ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/api/jobs/files/")
                        .path(String.valueOf(file.getId()))
                        .toUriString();

                return new ResponseFile(
                        file.getName(),
                        fileDownloadUri,
                        file.getType(),
                        file.getData().length);
            }).collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(files);
        }
        catch (JobExceptionHandler e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @GetMapping("/api/jobs/files/{id}")
    //@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
        Optional<DocumentFile> documentFile = service.getDocumentFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + documentFile.get().getName() + "\"")
                .body(documentFile.get().getData());
    }
}
