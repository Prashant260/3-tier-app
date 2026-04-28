package com.bloghub.repository;

import com.bloghub.entity.Author;
import com.bloghub.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByTitleContainingOrContentContaining(String title, String content);  // Search posts by title or content if any one matches return the post

    List<Post> findByAuthor(Author author);  // Get all posts by a specific author

}
