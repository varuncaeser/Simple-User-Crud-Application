package com.user_management.user_management_spring_boot.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.user_management.user_management_spring_boot.entity.UserInfo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represents a custom implementation of Spring Security's
 * UserDetails interface.
 * It is used to provide user-related information for authentication and
 * authorization purposes.
 * @author PUSHKAR D
 * @version 1.0
 */
public class UserInfoDetails implements UserDetails {

    /**
     * The username of the user.
     */
    private String username; // Changed from 'name' to 'username' for clarity

    /**
     * The password of the user.
     */
    private String password;

    /**
     * The authorities (roles) assigned to the user.
     */
    private List<GrantedAuthority> authorities;

    /**
     * Constructor that initializes the UserInfoDetails object with user
     * information.
     *
     * @param userInfo The UserInfo object containing user details.
     */
    public UserInfoDetails(UserInfo userInfo) {
        // this.username = userInfo.getName(); // Assuming 'name' is used as 'username'
        this.username = userInfo.getUserName();
        this.password = userInfo.getPassWord();
        this.authorities = List.of(userInfo.getRoles().split(","))
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
