package com.example.linkup.repositories;

import com.example.linkup.models.User;
import com.example.linkup.models.projection.UserSearchProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    User findByUsername(String username);

    @Query("SELECT u.id as id, u.firstName as firstName, u.lastName as lastName " +
            "FROM User u " +
            "WHERE u.status = 'ACTIVE' ")
    Page<UserSearchProjection> findAllUsersByActiveStatus(Pageable pageable);

    @Query("SELECT u.id as id, u.firstName as firstName, u.lastName as lastName " +
            "FROM User u " +
            "WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<UserSearchProjection> searchByFirstNameOrLastName(@Param("query") String query);
}
