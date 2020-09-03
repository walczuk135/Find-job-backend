package com.findjob.findjobgradle.controller;

import com.findjob.findjobgradle.controller.jobDto.JobDto;
import com.findjob.findjobgradle.domain.User;
import com.findjob.findjobgradle.service.UserExceptionHandler;
import com.findjob.findjobgradle.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user/")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<UserDetails> getUserInfo(Authentication authentication) {
        return ResponseEntity.ok(userService.getInfo(authentication.getName()));
    }

    @GetMapping("jobs/")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<JobDto>> getUserJobOffer(Authentication authentication) {
        return new ResponseEntity<>(userService.getYoursJobOffer(authentication.getName()), HttpStatus.OK);
    }

    @DeleteMapping()
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<User> deleteMyAccount(Authentication authentication) {
        userService.deleteMyAccount(authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable String username) throws UserExceptionHandler {
        Optional<User> existingUser = userService.findUserByName(username);
        return existingUser.map(user -> {
            userService.deleteUser(user.getUsername());
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}