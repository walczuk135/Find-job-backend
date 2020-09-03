package com.findjob.findjobgradle.repository;

import com.findjob.findjobgradle.domain.Job;
import com.findjob.findjobgradle.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("Select u From User u where  u.username = :username")
    User findByUsername(@Param("username") String username);

    @Query(value = "Select b.jobs from User b where b.username = :username")
    List<Job> findAllUserJobOffer(@Param("username") String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
