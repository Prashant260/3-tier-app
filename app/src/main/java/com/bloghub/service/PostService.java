package com.bloghub.service;

import com.bloghub.dto.PostRequestDto;
import com.bloghub.dto.PostResponseDto;
import com.bloghub.dto.PostUpdateDto;
import com.bloghub.entity.Author;
import com.bloghub.entity.Category;
import com.bloghub.entity.Post;
import com.bloghub.exception.ResourceNotFoundException;
import com.bloghub.repository.AuthorRepository;
import com.bloghub.repository.CategoryRepository;
import com.bloghub.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private PostRepository postRepository;
    private CategoryRepository categoryRepository;
    private AuthorRepository authorRepository;

    @Autowired
    public PostService(PostRepository postRepository, CategoryRepository categoryRepository, AuthorRepository authorRepository) {
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.authorRepository = authorRepository;
    }

    @Transactional
    public Post createPost(PostRequestDto request) {

        if (request.getAuthorId() == null) {// Added null check for authorId
            throw new IllegalArgumentException("Author ID must be provided by controller");// Updated exception message
        }

        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException
                                ("Author not found with ID: " +
                                        request.getAuthorId()));


        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException
                                ("Category not found with ID: " +
                                        request.getCategoryId()));

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setCreatedAt(LocalDateTime.now());
        post.setAuthor(author);
        post.setCategory(category);

        return postRepository.save(post);// Save and return the new post
    }


    public List<Post> getAllPosts() {// Method to get all posts
        return postRepository.findAll();// Retrieve and return all posts

    }


    public Page<PostResponseDto> getAllPostsWithPagination(int pageNo, int pageSize, String sortBy, String sortDir) {
        //Create Sort object
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        //Create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);// Create Pageable instance with page number, size, and sort

        Page<Post> postsPage = postRepository.findAll(pageable);// Fetch paginated posts from repository
        List<PostResponseDto> dtoList = new ArrayList<>();// List to hold PostResponseDto objects
        for (Post post : postsPage.getContent()) {// Convert each Post entity to PostResponseDto
            PostResponseDto dto = new PostResponseDto();// Create new DTO object
            dto.setId(post.getId());
            dto.setTitle(post.getTitle());
            dto.setContent(post.getContent());
            dto.setCreatedAt(post.getCreatedAt());
            dto.setAuthorId(post.getAuthor().getId());
            dto.setAuthorName(post.getAuthor().getName());
            dto.setCategoryId(post.getCategory().getId());
            dto.setCategoryName(post.getCategory().getCatName());
            dtoList.add(dto);// Add DTO to list
        }
// Create Page object for DTOs and returning original page details
        Page<PostResponseDto> pageList = new PageImpl<>(dtoList, pageable, postsPage.getTotalElements()); // Creating Page object for DTOs and returning original page details
        return pageList; // Return the page of PostResponseDto
    }


    public Post getPostById(Long postId) {// Method to get a post by ID
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + postId));
    }


    public List<Post> searchPosts(String keyword) {// Method to search posts by title or content
        return postRepository.findByTitleContainingOrContentContaining(keyword.toLowerCase(), keyword.toLowerCase());// Search and return matching posts
    }


    public List<Post> getPostByAuthor(Long authorId) {// Method to get posts by author ID
        Author author = authorRepository.findById(authorId).orElse(null);// Fetch author from repository
        if (author == null) {// Check if author is null
            throw new ResourceNotFoundException("Author not found with ID: " + authorId);
        }
        return postRepository.findByAuthor(author);// Fetch and return posts by author
    }

    @Transactional
    public Post updatePost(Long postId, PostUpdateDto postUpd) {// Method to update a post
        Post post = getPostById(postId);// Fetch existing post

        if (postUpd == null || (postUpd.getTitle() == null && postUpd.getAuthorId() == null && postUpd.getCategoryId() == null && postUpd.getContent() == null)) {
            throw new IllegalArgumentException("At least one field must be provided for update");

        }
        if (postUpd.getTitle() != null) {
            post.setTitle(postUpd.getTitle());// Update title if provided
        }
        if (postUpd.getContent() != null) {
            post.setContent(postUpd.getContent());// Update content if provided
        }
        if (postUpd.getAuthorId() != null) {
            Author author = authorRepository.findById(postUpd.getAuthorId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Author not found with ID: " + postUpd.getAuthorId()));
            post.setAuthor(author);// Update author if provided
        }
        if (postUpd.getCategoryId() != null) {
            Category category = categoryRepository.findById(postUpd.getCategoryId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Category not found with ID: " + postUpd.getCategoryId()));
            post.setCategory(category);// Update category if provided
        }
        return postRepository.save(post);// Save and return updated post
    }

    @Transactional
    public void deletePost(Long postId) {// Method to delete a post
        Post post = getPostById(postId);// Fetch existing post
        postRepository.delete(post);// Delete the fetched post
    }

}