package com.user_management.user_management_spring_boot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents a response from the user management system.
 * It contains two fields: status and userId.
 * @author PUSHKAR D
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    /**
     * Represents the status of the operation.
     * It can have values like "success", "failure", "error", etc.
     */
    private String status;

    /**
     * Represents the unique identifier of the user.
     * It is an integer value and can be null if the user is not found.
     */
    private Integer userId;
}
