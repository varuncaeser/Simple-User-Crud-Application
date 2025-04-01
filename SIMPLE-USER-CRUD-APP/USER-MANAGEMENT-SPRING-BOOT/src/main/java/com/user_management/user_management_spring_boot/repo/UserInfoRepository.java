package com.user_management.user_management_spring_boot.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.user_management.user_management_spring_boot.entity.UserInfo;

import java.util.Optional;
// import java.util.List;

/**
 * Repository interface for managing {@link UserInfo} entities.
 * Provides methods for CRUD operations and custom queries.
 * @author PUSHKAR D
 * @version 1.0
 */
@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {

        /**
         * Finds a user by their username.
         *
         * @param userName the username to search for
         * @return an {@link Optional} containing the user if found, otherwise
         *         {@link Optional#empty()}
         */
        Optional<UserInfo> findByUserName(String userName);

        /**
         * Checks if a user with the given username exists.
         *
         * @param userName the username to check
         * @return {@code true} if a user with the given username exists, otherwise
         *         {@code false}
         */
        boolean existsByUserName(String userName);

        /**
         * Finds users based on the provided search criteria.
         *
         * @param userId    the user ID to search for (optional, can be {@code null})
         * @param firstName the first name to search for (optional, can be {@code null})
         * @param lastName  the last name to search for (optional, can be {@code null})
         * @param email     the email to search for (optional, can be {@code null})
         * @param userName  the username to search for (optional, can be {@code null})
         * @param pageable  the pagination and sorting information
         * @return a {@link Page} of {@link UserInfo} matching the search criteria
         */
        @Query("SELECT u FROM UserInfo u WHERE " +
                        "( :userId IS NULL OR u.id = :userId ) AND " +
                        "( :firstName IS NULL OR u.firstName LIKE %:firstName% ) AND " +
                        "( :lastName IS NULL OR u.lastName LIKE %:lastName% ) AND " +
                        "( :email IS NULL OR u.email LIKE %:email% ) AND " +
                        "( :userName IS NULL OR u.userName LIKE %:userName% ) " +
                        "ORDER BY u.id ASC")
        Page<UserInfo> findUsers(
                        @Param("userId") Integer userId,
                        @Param("firstName") String firstName,
                        @Param("lastName") String lastName,
                        @Param("email") String email,
                        @Param("userName") String userName,
                        Pageable pageable);

}
