package com.findjob.findjobgradle.repository;

import com.findjob.findjobgradle.domain.Category;
import com.findjob.findjobgradle.domain.Job;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    @Query("Select j From Job j")
    List<Job> findAllJobPageable(Pageable page);

    @Query("Select j From Job j where  j.category = :category")
    List<Job> findAllJobByCategory(@Param("category") Category category, Pageable pageable);

    @Query("Select j From Job j where  j.city = :city")
    List<Job> findAllJobByCity(@Param("city") String city, Pageable pageable);

    @Query("Select j From Job j where  j.title = :title")
    List<Job> findAllJobByTitle(@Param("title") String title, Pageable pageable);

    @Query("Select j From Job j where  j.company = :company")
    List<Job> findAllJobByCompany(@Param("company") String company, Pageable pageable);

    @Query("Select j From Job j where  j.category = :category and j.city = :city")
    List<Job> findAllJobByCategoryAndCity(@Param("category") Category category, @Param("city") String city, Pageable pageable);

    boolean existsJobById(Long id);

    //List<Job> findAllByTitleContainsIgnoreCase(String title);

    List<Job> findAllByTitleContainsIgnoreCase(String title, Pageable pageable);

    List<Job> findByPublished(boolean published);


    boolean existsJobByIdAndUserId(long idJob, long idUser);
}
