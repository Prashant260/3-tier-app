package com.bloghub.controller;

import com.bloghub.dto.PostRequestDto;
import com.bloghub.dto.PostResponseDto;
import com.bloghub.dto.PostUpdateDto;
import com.bloghub.entity.Post;
import com.bloghub.service.PostService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController// Marks this class as a REST controller
@RequestMapping("/api/posts")// Base URL for Post-related endpoints
public class PostController {

    private PostService postService;  // Inject PostService

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping// Handles POST requests to create a new post
    public ResponseEntity<PostResponseDto> createPost(
            @RequestBody @Valid PostRequestDto request,
            HttpSession session) {
        Long currentUserId = (Long) session.getAttribute("userId");
        request.setAuthorId(currentUserId);// Set the author ID from the session
        Post post = postService.createPost(request); // Create the post using PostService
        PostResponseDto response = new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCategory().getCatName(),
                post.getCategory().getId(),
                post.getAuthor().getName(),
                post.getAuthor().getId(),
                post.getCreatedAt());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/getAll")// Handles GET requests to retrieve all posts
    public ResponseEntity<List<PostResponseDto>> getAllPosts(@RequestParam(required = false) String term) {
        List<Post> posts;
        if (term != null && !term.isBlank()) {
            posts = postService.searchPosts(term);
        } else {
            posts = postService.getAllPosts();
        }

        List<PostResponseDto> respList = new ArrayList<>();
        for (Post post : posts) {
            PostResponseDto dto = new PostResponseDto(
                    post.getId(),
                    post.getTitle(),
                    post.getContent(),
                    post.getCategory().getCatName(),
                    post.getCategory().getId(),
                    post.getAuthor().getName(),
                    post.getAuthor().getId(),
                    post.getCreatedAt());
            respList.add(dto);
        }
        return ResponseEntity.ok(respList);
    }

    @GetMapping// Handles GET requests to retrieve paginated posts
    public ResponseEntity<Page<PostResponseDto>> getPostsPaginated(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "3") int pageSize,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        return ResponseEntity.ok(postService.getAllPostsWithPagination(pageNo, pageSize, sortBy, sortDir));// Retrieve paginated posts using PostService
    }


    @GetMapping("/{postId}")// Handles GET requests to retrieve a post by ID
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long postId) {
        Post post = postService.getPostById(postId);// Retrieve the post using PostService
        PostResponseDto response = new PostResponseDto();
        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setCategoryId(post.getCategory().getId());
        response.setCategoryName(post.getCategory().getCatName());
        response.setAuthorId(post.getAuthor().getId());
        response.setAuthorName(post.getAuthor().getName());
        response.setCreatedAt(post.getCreatedAt());

        return ResponseEntity.ok(response);// Return the post details
    }

    @GetMapping("/myPosts")// Handles GET requests to retrieve posts by the current user
    public ResponseEntity<List<PostResponseDto>> getMyPosts(
            @RequestAttribute("currentUserID") Long currentUserId) {

        List<Post> postList = postService.getPostByAuthor(currentUserId);

        List<PostResponseDto> response = postList.stream().map(post -> {
            PostResponseDto dto = new PostResponseDto();
            dto.setId(post.getId());
            dto.setTitle(post.getTitle());
            dto.setContent(post.getContent());
            dto.setCreatedAt(post.getCreatedAt());
            dto.setAuthorId(post.getAuthor().getId());
            dto.setAuthorName(post.getAuthor().getName());
            dto.setCategoryId(post.getCategory().getId());
            dto.setCategoryName(post.getCategory().getCatName());
            return dto;
        }).toList();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{postId}")// Handles PUT requests to update a post by ID
    public ResponseEntity<?> updatePost(
            @PathVariable Long postId,
            @RequestBody @Valid PostUpdateDto request,
            @RequestAttribute("currentUserId") Long currentUserId,
            @RequestAttribute("currentUserRole") String currentUserRole) {
        Post post = postService.getPostById(postId); // Update the post using PostService
        if (!post.getAuthor().getId().equals(currentUserId) && !currentUserRole.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"error\":\"You are not authorized to update this post.\"}");
        }// Check if the current user is the author or an admin
        Post updatedPost = postService.updatePost(postId, request);
        PostResponseDto response = new PostResponseDto(
                updatedPost.getId(),
                updatedPost.getTitle(),
                updatedPost.getContent(),
                updatedPost.getCategory().getCatName(),
                updatedPost.getCategory().getId(),
                updatedPost.getAuthor().getName(),
                updatedPost.getAuthor().getId(),
                updatedPost.getCreatedAt());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}")// Handles DELETE requests to delete a post by ID
    public ResponseEntity<?> deletePost(
            @PathVariable Long postId,
            @RequestAttribute("currentUserId") Long currentUserId,
            @RequestAttribute("currentUserRole") String currentUserRole) {

        if (currentUserId == null || currentUserRole == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"error\":\"You are not authorized to delete this post.\"}");
        }
        //Admin can delete any post, author can delete their own post
        Post existingPost = postService.getPostById(postId);
        if (!currentUserRole.equals("ADMIN") && existingPost.getAuthor().getId().equals(currentUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"error\":\"You are not authorized to delete this post.\"}");
        }
        postService.deletePost(postId);
        return ResponseEntity.ok("{\"message\":\"Post deleted successfully...\"}");

    }
}
