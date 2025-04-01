package com.user_management.user_management_spring_boot.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.user_management.user_management_spring_boot.entity.JwtAudit;
import com.user_management.user_management_spring_boot.entity.UserInfo;

/**
 * This interface represents a repository for managing JWT audit records. It extends the JpaRepository interface,
 * which provides CRUD operations and additional methods for querying the database.
 *
 * @author PUSHKAR D
 * @version 1.0
 */
@Repository
public interface JwtAuditRepository extends JpaRepository<JwtAudit, Long> {

    /**
     * Finds a JwtAudit record by the given token.
     *
     * @param token The unique token to search for.
     * @return An Optional containing the JwtAudit record if found, or an empty Optional if not found.
     */
    Optional<JwtAudit> findByToken(String token);

    /**
     * Finds all JwtAudit records associated with the given user.
     *
     * @param user The UserInfo object representing the user.
     * @return A List of JwtAudit records associated with the given user.
     */
    List<JwtAudit> findAllByUser(UserInfo user);
}
