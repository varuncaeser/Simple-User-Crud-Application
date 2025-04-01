package com.user_management.user_management_spring_boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.user_management.user_management_spring_boot.entity.UserInfo;
import com.user_management.user_management_spring_boot.entity.UserInfoResponseAPI;
import com.user_management.user_management_spring_boot.entity.UserQueryParams;
import com.user_management.user_management_spring_boot.repo.UserInfoRepository;

// import java.util.ArrayList;
import java.util.List;
// import java.util.Optional;

/**
 * This class provides services related to user information.
 * It implements the UserDetailsService interface to handle user authentication.
 *
 * @author PUSHKAR D
 * @version 1.0
 */
@Service
public class UserInfoService implements UserDetailsService {

    /**
     * Repository for accessing user information data.
     */
    @Autowired
    private UserInfoRepository repository;

    /**
     * Password encoder for securely storing passwords.
     */
    @Autowired
    private PasswordEncoder encoder;

    /**
     * {@inheritDoc}
     *
     * Loads user details by username.
     *
     * @param username The username of the user to load.
     * @return The loaded user details.
     * @throws UsernameNotFoundException If the user with the given username is not
     *                                   found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo user = repository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassWord(),
                List.of(new SimpleGrantedAuthority(user.getRoles())));
    }

    /**
     * Adds a new user to the system.
     *
     * @param userInfo The user information to add.
     * @return The ID of the newly added user.
     */
    public Integer addUser(UserInfo userInfo) {
        if (usernameExists(userInfo.getUserName())) {
            throw new IllegalArgumentException("Username already exists: " + userInfo.getUserName());
        }
        // Encode password before saving the user
        userInfo.setPassWord(encoder.encode(userInfo.getPassWord()));
        repository.save(userInfo);
        return userInfo.getId();
    }

    /**
     * Checks if a username already exists in the system.
     *
     * @param username The username to check.
     * @return True if the username exists, false otherwise.
     */
    public boolean usernameExists(String username) {
        return repository.existsByUserName(username);
    }

    /**
     * Queries users based on the given query parameters.
     *
     * @param queryParams The query parameters to filter users.
     * @param pageable    The pagination information.
     * @return The page of user information matching the query parameters.
     */
    public Page<UserInfoResponseAPI> queryUsers(UserQueryParams queryParams, Pageable pageable) {
        Page<UserInfo> users = repository.findUsers(queryParams.getUserId(), queryParams.getFirstName(),
                queryParams.getLastName(), queryParams.getEmail(), queryParams.getUserName(), pageable);
        return users.map(user -> {
            UserInfoResponseAPI response = new UserInfoResponseAPI();
            response.setId(user.getId());
            response.setUserName(user.getUserName());
            response.setFirstName(user.getFirstName());
            response.setLastName(user.getLastName());
            response.setEmail(user.getEmail());
            response.setRoles(user.getRoles());
            return response;
        });
    }

    /**
     * Retrieves all users.
     *
     * @param pageable The pagination information.
     * @return The page of all user information.
     */
    public Page<UserInfoResponseAPI> getUsers(Pageable pageable) {
        Page<UserInfo> users = repository.findAll(pageable);
        return users.map(user -> {
            UserInfoResponseAPI response = new UserInfoResponseAPI();
            response.setId(user.getId());
            response.setUserName(user.getUserName());
            response.setFirstName(user.getFirstName());
            response.setLastName(user.getLastName());
            response.setEmail(user.getEmail());
            response.setRoles(user.getRoles());
            return response;
        });
    }

}