package com.user_management.user_management_spring_boot.entity;

import java.util.Date;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

/**
 * Represents a JWT (JSON Web Token) audit record in the database.
 * This entity is used to track the usage and validity of JWT tokens issued to users.
 *
 * @author PUSHKAR D
 * @since 1.0
 */
@Entity
@Data
@Table(name = "jwt_audit")
public class JwtAudit {

    /**
     * The unique identifier for the JWT audit record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The user associated with the JWT token.
     * This field is fetched lazily for efficiency.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfo user;

    /**
     * The JWT token itself.
     */
    @Column(nullable = false)
    private String token;

    /**
     * The timestamp when the JWT token was issued.
     * This field is automatically set to the current date and time when a new record is created.
     */
    @Column(name = "issued_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime issuedAt = LocalDateTime.now();

    /**
     * The timestamp when the JWT token will expire.
     */
    @Column(name = "expiry", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiry;

    /**
     * Indicates whether the JWT token is currently valid.
     * This field is initially set to true when a new record is created.
     */
    @Column(name = "is_valid", nullable = false)
    private Boolean isValid = true;
}