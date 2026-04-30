package com.bloghub.controller;

import com.bloghub.dto.AuthorRequestDto;
import com.bloghub.dto.AuthorResponseDto;
import com.bloghub.dto.AuthorUpdateDto;
import com.bloghub.entity.Author;
import com.bloghub.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController// Indicates that this class is a REST controller in the Spring framework
@RequestMapping("/api/users")// Base URL mapping for author-related endpoints
public class AuthorController {

    private final AuthorService authorService;// Service for author-related business logic

    public AuthorController(AuthorService authorService) {// Constructor injection of the AuthorService
        this.authorService = authorService;// Initialize the service
    }

    @GetMapping("/{id}")// Maps HTTP GET requests to this method for retrieving an author by ID
    public ResponseEntity<AuthorResponseDto> getAuthorById(@PathVariable Long id) {
        Author author = authorService.getAuthorById(id);// Fetch author details using the service
        AuthorResponseDto authorResponseDto = new AuthorResponseDto(id, author.getName(), author.getEmail(), author.getRole(), author.getAbout());// Create a DTO for the author response
        return ResponseEntity.ok(authorResponseDto);// Return the author details in the response
    }

    @PostMapping
    public ResponseEntity<AuthorResponseDto> createUser(@RequestBody @Valid AuthorRequestDto authorRequestDto) {
        Author author = authorService.createAuthor(authorRequestDto);
        AuthorResponseDto authorResponseDto = new AuthorResponseDto(author.getId(), author.getName(), author.getEmail(), author.getRole(), author.getAbout());
        return new ResponseEntity<>(authorResponseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")// Maps HTTP PUT requests to this method for updating an author by ID
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody AuthorUpdateDto authorUpdateDto) {
        Author updatedAuthor = authorService.updateAuthorById(id, authorUpdateDto);// Update the author using the service
        AuthorResponseDto authorResponseDto = new AuthorResponseDto(updatedAuthor.getId(), updatedAuthor.getName(), updatedAuthor.getEmail(), updatedAuthor.getRole(), updatedAuthor.getAbout());// Create a DTO for the updated author response
        return ResponseEntity.ok(authorResponseDto);// Return the updated author details in the response
    }

    @DeleteMapping("/{id}")// Maps HTTP DELETE requests to this method for deleting an author by ID
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        authorService.deleteAuthorById(id);// Delete the author using the service
        return ResponseEntity.ok("{\"message\":\"Author deleted successfully.\"}");// Return a success message
    }

    @GetMapping// Maps HTTP GET requests to this method for retrieving all authors
    public ResponseEntity<List<AuthorResponseDto>> getAllAuthors() {
        List<Author> authors = authorService.getAllAuthors();// Fetch all authors using the service
        List<AuthorResponseDto> authorResponseDtos = authors.stream()
                .map(author -> new AuthorResponseDto(author.getId(), author.getName(), author.getEmail(), author.getRole(), author.getAbout()))// Create DTOs for each author
                .toList();
        return ResponseEntity.ok(authorResponseDtos);// Return the list of authors in the response
    }

}
