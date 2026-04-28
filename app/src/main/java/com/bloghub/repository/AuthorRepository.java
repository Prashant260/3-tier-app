package com.bloghub.repository;

import com.bloghub.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    boolean existsByEmail(String email);                                // Check if an author exists by email

    Optional<Author> findByEmail(String email);// Find an author by email
}

