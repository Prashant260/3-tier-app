package com.bloghub.controller;

import com.bloghub.dto.AuthResponseDto;
import com.bloghub.dto.LoginRequestDto;
import com.bloghub.dto.RegisterRequestDto;
import com.bloghub.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//This annotation indicates that this class is a RESTful controller, meaning it will handle HTTP requests and return responses in a RESTful manner.
@RequestMapping("/api/auth")
//This annotation indicates that this class will handle HTTP requests related to authentication, and the base URL for these requests will be /api/auth.
  
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {//This constructor uses the @Autowired annotation to inject an instance of AuthService into the AuthController.
        this.authService = authService;//This line assigns the injected AuthService instance to the authService field of the AuthController class.
    }


    @PostMapping("/register")
    //This annotation indicates that this method will handle HTTP POST requests sent to the /register endpoint
    public ResponseEntity<AuthResponseDto> register(@RequestBody @Valid RegisterRequestDto request) {//This method handles HTTP requests for user registration. It takes a RegisterRequestDto object as input, which is validated using the @Valid annotation.
        AuthResponseDto response = authService.register(request);//This line calls the register method of the AuthService, passing the request object, and stores the returned AuthResponseDto in the response variable.
        return new ResponseEntity<>(response, HttpStatus.CREATED);//This line creates a new ResponseEntity object containing the response and an HTTP status code of 201 (Created), indicating that the registration was successful.
    }

    @PostMapping("/login")
    //This annotation indicates that this method will handle HTTP POST requests sent to the /login endpoint
    public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid LoginRequestDto request, HttpSession session) {//This method handles HTTP requests for user login. It takes a RegisterRequestDto object as input, which is validated using the @Valid annotation, and an HttpSession object to manage the user's session.
        AuthResponseDto response = authService.login(request, session);//This line calls the login method of the AuthService, passing the request object and session, and stores the returned AuthResponseDto in the response variable.
        return ResponseEntity.ok(response);//This line creates a new ResponseEntity object containing the response and an HTTP status code of 200 (OK), indicating that the login was successful.
    }

    @PostMapping("/logout")
    //This annotation indicates that this method will handle HTTP POST requests sent to the /logout endpoint
    public ResponseEntity<String> logout(HttpSession session) {//This method handles HTTP requests for user logout. It takes an HttpSession object to manage the user's session
        authService.logout(session);//This line calls the logout method of the AuthService, passing the session object to invalidate the user's session.
        return ResponseEntity.ok("Logged out successfully...");//This line creates a new
    }

    @GetMapping("/me")
//This annotation indicates that this method will handle HTTP GET requests sent to the /me endpoint
    public ResponseEntity<AuthResponseDto> getCurrentUser(HttpSession session) {//This method handles HTTP requests to retrieve the currently logged-in user's information. It takes an HttpSession object to manage the user's session.
        AuthResponseDto response = authService.getCurrentUser(session);//This line calls the getCurrentUser method of the AuthService, passing the session object, and stores the returned AuthResponseDto in the response variable.
        return ResponseEntity.ok(response);//This line creates a new ResponseEntity object containing the response and an HTTP status code of 200 (OK), indicating that the request was successful.
    }
}

