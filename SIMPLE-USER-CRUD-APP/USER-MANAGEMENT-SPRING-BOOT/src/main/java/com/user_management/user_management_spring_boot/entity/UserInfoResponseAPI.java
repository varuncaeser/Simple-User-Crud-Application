package com.user_management.user_management_spring_boot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents the response structure for user information.
 * It includes fields such as user ID, username, first name, last name, email,
 * and roles.
 * @author PUSHKAR D
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponseAPI {

    /**
     * The unique identifier of the user.
     */
    private Integer id;

    /**
     * The username of the user.
     */
    private String userName;

    /**
     * The first name of the user.
     */
    private String firstName;

    /**
     * The last name of the user.
     */
    private String lastName;

    /**
     * The email address of the user.
     */
    private String email;

    /**
     * The roles assigned to the user, represented as a comma-separated string.
     */
    private String roles;
}
