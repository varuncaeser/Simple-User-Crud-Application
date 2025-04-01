package com.user_management.user_management_spring_boot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a request for authentication.
 * This class is used to encapsulate the necessary information for user
 * authentication.
 * @author PUSHKAR D
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    /**
     * The username of the user attempting to authenticate.
     */
    private String userName;

    /**
     * The password of the user attempting to authenticate.
     */
    private String passWord;

}
