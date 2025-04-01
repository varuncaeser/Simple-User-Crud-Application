package com.user_management.user_management_spring_boot.controller;

import java.util.Base64;
// import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;

import com.user_management.user_management_spring_boot.entity.AuthRequest;
import com.user_management.user_management_spring_boot.entity.UserInfo;
import com.user_management.user_management_spring_boot.entity.UserInfoResponseAPI;
import com.user_management.user_management_spring_boot.entity.UserQueryParams;
import com.user_management.user_management_spring_boot.entity.UserResponse;
import com.user_management.user_management_spring_boot.service.JwtService;
import com.user_management.user_management_spring_boot.service.UserInfoService;

import jakarta.validation.Valid;

/**
 * The UserController class is a Spring REST controller that provides APIs for
 * managing users
 * and handling user authentication in a web application. It integrates with
 * JWT-based
 * authentication and also supports Basic Authentication for legacy use cases.
 *
 * This controller includes endpoints for:
 * 1. Adding new users with validation and duplicate username checks.
 * 2. Generating JWT tokens for authenticated users.
 * 3. Retrieving and querying user information with pagination and
 * authentication.
 *
 * The controller leverages Spring's Dependency Injection to interact with
 * services such as:
 * - UserInfoService: For handling user-related business logic.
 * - JwtService: For generating and validating JWT tokens.
 * - AuthenticationManager: For authenticating users using provided credentials.
 *
 * Security:
 * - Authentication headers are required for accessing protected resources.
 * - Supports both JWT tokens (via `Bearer` prefix) and Basic Authentication
 * (via `Basic` prefix).
 *
 * CORS:
 * - The controller is configured to allow cross-origin requests from
 * `http://localhost:3000/`.
 *
 * Endpoints:
 * - `POST /auth/addNewUser` - Adds a new user with validation.
 * - `POST /auth/generateToken` - Authenticates a user and generates a JWT
 * token.
 * - `GET /auth/users` - Retrieves a paginated list of users.
 * - `POST /auth/queryUsers` - Queries users based on filter criteria with
 * pagination.
 *
 * Dependency Annotations:
 * - `@RestController`: Denotes this class as a REST API controller.
 * - `@RequestMapping`: Specifies the base path for all endpoints.
 * - `@CrossOrigin`: Configures cross-origin requests to allow interaction with
 * the frontend.
 *
 * Assumptions:
 * - Users are uniquely identified by their username.
 * - JWT tokens are used for stateless authentication.
 * - The frontend application is hosted on `http://localhost:3000/`.
 *
 * Potential Enhancements:
 * - Add detailed error logging for debugging production issues.
 * - Support more flexible CORS configurations for broader deployment scenarios.
 *
 * @author PUSHKAR D
 * @version 1.0
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000/")
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserInfoService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * This function handles the POST request to add a new user to the system.
     *
     * @param userInfo      The user information to be added. This should be a valid
     *                      {@link UserInfo} object.
     * @param bindingResult The result of the validation of the {@param userInfo}
     *                      object.
     *
     * @return A {@link ResponseEntity} containing a {@link UserResponse} object.
     *         If the {@param userInfo} object is valid and the username is unique,
     *         the response will contain a success message and the ID of the newly
     *         created user.
     *         If the {@param userInfo} object is not valid, the response will
     *         contain a bad request status and a validation error message.
     *         If the username already exists, the response will contain a bad
     *         request status and a message indicating that the username already
     *         exists.
     *         If an error occurs while saving the user, the response will contain
     *         an internal server error status and a message indicating that the
     *         user creation failed.
     */
    @PostMapping("/addNewUser")
    public ResponseEntity<UserResponse> addNewUser(@Valid @RequestBody UserInfo userInfo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new UserResponse("Validation failed: " + errorMessage, null));
        }

        // Check for unique username
        if (service.usernameExists(userInfo.getUserName())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new UserResponse("Username already exists", null));
        }

        // Save user and return response
        Integer userId = service.addUser(userInfo);

        if (userId != null) {
            return ResponseEntity.ok(new UserResponse("success", userId));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new UserResponse("User creation failed", null));
        }
    }

    /**
     * Authenticates the user and generates a JWT token for subsequent API requests.
     *
     * @param authRequest The authentication request containing the username and
     *                    password.
     *                    This should be a valid {@link AuthRequest} object.
     *
     * @return A JWT token as a string. If the authentication is successful, the
     *         token will be returned.
     *         If the authentication fails, an exception will be thrown.
     *
     * @throws UsernameNotFoundException If the authentication fails due to invalid
     *                                   user credentials.
     */
    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassWord()));
            if (authentication.isAuthenticated()) {
                return jwtService.generateToken(authRequest.getUserName());
            } else {
                throw new UsernameNotFoundException("Invalid user request!");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace for debugging
            throw new UsernameNotFoundException("Authentication failed: " + e.getMessage());
        }
    }

    /**
     * Retrieves a paginated list of user information based on the provided
     * authentication header and pagination parameters.
     *
     * @param authHeader The authentication header containing the JWT token or Basic
     *                   Auth credentials.
     *                   This parameter is optional and can be null if no
     *                   authentication is required.
     * @param pageable   The pagination parameters including page number, page size,
     *                   sorting criteria, etc.
     *
     * @return A ResponseEntity containing a Page of UserInfoResponseAPI objects.
     *         If the authentication is successful, the response will contain a
     *         status code of 200 (OK) and the paginated list of user information.
     *         If the authentication fails, the response will contain a status code
     *         of 401 (UNAUTHORIZED).
     *
     * @throws IllegalArgumentException If the pageable parameter is null.
     */
    @GetMapping("/users")
    public ResponseEntity<Page<UserInfoResponseAPI>> getUsers(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @PageableDefault(size = 10) Pageable pageable) {
        if (!isAuthenticated(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Page<UserInfoResponseAPI> users = service.getUsers(pageable);
        return ResponseEntity.ok(users);
    }

    /**
     * Handles the POST request to retrieve a paginated list of user information
     * based on the provided query parameters and authentication header.
     *
     * @param queryParams The query parameters to filter the user information. This
     *                    should be a valid {@link UserQueryParams} object.
     * @param authHeader  The authentication header containing the JWT token or
     *                    Basic Auth credentials.
     *                    This parameter is optional and can be null if no
     *                    authentication is required.
     * @param pageable    The pagination parameters including page number, page
     *                    size, sorting criteria, etc.
     *
     * @return A ResponseEntity containing a Page of UserInfoResponseAPI objects.
     *         If the authentication is successful, the response will contain a
     *         status code of 200 (OK) and the paginated list of user information
     *         that matches the query parameters.
     *         If the authentication fails, the response will contain a status code
     *         of 401 (UNAUTHORIZED).
     *
     * @throws IllegalArgumentException If the pageable parameter is null.
     */
    @PostMapping("/queryUsers")
    public ResponseEntity<Page<UserInfoResponseAPI>> queryUsers(@RequestBody UserQueryParams queryParams,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @PageableDefault(size = 10) Pageable pageable) {
        if (!isAuthenticated(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Page<UserInfoResponseAPI> matchingUsers = service.queryUsers(queryParams, pageable);
        return ResponseEntity.ok(matchingUsers);
    }

    /**
     * Handles the POST request to log out the user by invalidating the JWT token.
     *
     * @param authHeader The authentication header containing the JWT token.
     *                   This parameter is optional and can be null if no
     *                   authentication is required.
     *
     * @return A ResponseEntity containing a success message.
     *         If the authentication header is valid and the JWT token is successfully
     *         invalidated, the response will contain a status code of 200 (OK) and
     *         the message "Token successfully invalidated."
     *         If the authentication header is invalid or the JWT token is not provided,
     *         the response will contain a status code of 401 (UNAUTHORIZED) and
     *         the message "Invalid authorization header".
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid authorization header");
        }
        String token = authHeader.substring(7); // Extract the JWT token
        jwtService.invalidateToken(token); // Invalidate the token
        return ResponseEntity.ok("Token successfully invalidated.");
    }

    /**
     * Validates the authentication header to determine if the user is
     * authenticated.
     *
     * @param authHeader The authentication header containing the JWT token or Basic
     *                   Auth credentials.
     *                   This parameter is optional and can be null if no
     *                   authentication is required.
     *
     * @return True if the user is authenticated, false otherwise.
     *         If the authentication header starts with "Bearer ", the function
     *         extracts the JWT token and validates it using the {@link JwtService}.
     *         If the authentication header starts with "Basic ", the function
     *         extracts the encoded credentials, decrypts them, and verifies them
     *         using the {@link AuthenticationManager}.
     *         If the authentication header does not start with either "Bearer " or
     *         "Basic ", the function returns false.
     */
    private boolean isAuthenticated(String authHeader) {
        if (authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Extract the JWT token
            return jwtService.validateToken(token); // Validate the JWT token
        } else if (authHeader.startsWith("Basic ")) {
            String encodedCredentials = authHeader.substring(6); // Extract the encoded credentials
            return isValidBasicAuthCredentials(encodedCredentials); // Validate Basic Auth credentials
        }
        return false; // Invalid authentication method
    }

    /**
     * Validates the Basic Auth credentials by decrypting the Base64 encoded
     * username:password and authenticating the user.
     *
     * @param encodedCredentials The Base64 encoded username:password credentials.
     *
     * @return True if the credentials are valid, false otherwise.
     *         The function decrypts the Base64 encoded credentials, splits them
     *         into username and password, and authenticates the user using the
     *         {@link AuthenticationManager}.
     *         If the authentication is successful, the function returns true.
     *         If the authentication fails, the function returns false.
     */
    private boolean isValidBasicAuthCredentials(String encodedCredentials) {
        // Decrypt the Base64 encoded username:password and verify the credentials
        String decodedCredentials = new String(Base64.getDecoder().decode(encodedCredentials));
        String[] credentials = decodedCredentials.split(":");
        String username = credentials[0];
        String password = credentials[1];

        // You should now authenticate the user with these credentials
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return true; // Credentials are valid
        } catch (AuthenticationException e) {
            return false; // Authentication failed
        }
    }
}
