package com.user_management.user_management_spring_boot.entity;

// import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents a user's information and is used as an entity in the
 * database.
 * It is annotated with JPA annotations to define the table name and field
 * mappings.
 *
 * @author PUSHKAR D
 * @version 1.0
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_table1")
public class UserInfo {

    /**
     * The unique identifier for each user.
     * It is auto-generated by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The username of the user.
     * It is required and should be between 3 and 20 characters long.
     */
    @NotNull(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username should be between 3 and 20 characters")
    private String userName;

    /**
     * The first name of the user.
     * It is required.
     */
    @NotNull(message = "First name is required")
    private String firstName;

    /**
     * The last name of the user.
     * It is required.
     */
    @NotNull(message = "Last name is required")
    private String lastName;

    /**
     * The email address of the user.
     * It should be a valid email address.
     */
    @Email(message = "Email should be valid")
    private String email;

    /**
     * The password of the user.
     * It is required, should be at least 8 characters long, and should have at
     * least one uppercase letter, one number, and one special character.
     */
    // @JsonIgnore
    @NotNull(message = "Password is required")
    @Size(min = 8, message = "Password should be at least 8 characters long")
    @Pattern(regexp = "(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=]).*", message = "Password should have at least one uppercase letter, one number, and one special character")
    private String passWord;

    /**
     * The roles assigned to the user.
     */
    private String roles;
}
