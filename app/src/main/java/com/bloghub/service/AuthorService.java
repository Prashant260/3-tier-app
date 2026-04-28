package com.bloghub.service;

import com.bloghub.dto.AuthorUpdateDto;
import com.bloghub.entity.Author;
import com.bloghub.exception.ResourceNotFoundException;
import com.bloghub.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service// Indicates that this class is a service component in the Spring framework
public class AuthorService {
    private AuthorRepository authorRepository;// Repository for author data access

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {// Constructor injection of the AuthorRepository
        this.authorRepository = authorRepository;// Initialize the repository
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();// Retrieve all authors from the repository
    }

    public Author getAuthorById(Long id) {
        Author author = authorRepository.findById(id).orElse(null);// Fetch author by ID
        if (author == null) {// Check if author exists
            throw new ResourceNotFoundException("Author not found with id: " + id);// Throw exception if not found
        }

        return author;// Retrieve an author by ID, throwing an exception if not found
    }

    public void deleteAuthorById(Long id) {
        Author author = getAuthorById(id);// Fetch author by ID
        authorRepository.deleteById(id);// Delete the author by ID
    }

    public Author updateAuthorById(Long id, AuthorUpdateDto authorUpdateDto) {
        Author author = getAuthorById(id);// Fetch author by ID

        // Update author fields if they are provided in the DTO
        if (authorUpdateDto.getName() == null && authorUpdateDto.getAbout() == null && authorUpdateDto.getEmail() == null) {
            throw new RuntimeException("At least one field (name, about, email) must be provided for update.");// Ensure at least one field is provided
        }
        if (authorUpdateDto.getName() != null && authorUpdateDto.getName().isBlank()) {
            throw new RuntimeException("Name cannot be blank.");// Validate name field
        }
        if (authorUpdateDto.getAbout() != null && authorUpdateDto.getAbout().isBlank()) {
            throw new RuntimeException("About cannot be blank.");// Validate about field
        }
        if (authorUpdateDto.getName() != null)
            author.setName(authorUpdateDto.getName());// Update name if provided
        if (authorUpdateDto.getAbout() != null)
            author.setAbout(authorUpdateDto.getAbout());// Update about if provided
        if (authorUpdateDto.getEmail() != null)
            author.setEmail(authorUpdateDto.getEmail());// Update email if provided
        
        return authorRepository.save(author);// Save and return the updated author
    }
}
