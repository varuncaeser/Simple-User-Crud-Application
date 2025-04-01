package com.user_management.user_management_spring_boot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents the query parameters for filtering user data.
 * It is used to encapsulate the search criteria for retrieving user
 * information.
 * @author PUSHKAR D
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserQueryParams {

    /**
     * The unique identifier of the user.
     * If provided, only users with this specific ID will be returned.
     */
    private Integer userId;

    /**
     * The first name of the user.
     * If provided, only users with this specific first name will be returned.
     */
    private String firstName;

    /**
     * The last name of the user.
     * If provided, only users with this specific last name will be returned.
     */
    private String lastName;

    /**
     * The email address of the user.
     * If provided, only users with this specific email address will be returned.
     */
    private String email;

    /**
     * The username of the user.
     * If provided, only users with this specific username will be returned.
     */
    private String userName;
}
