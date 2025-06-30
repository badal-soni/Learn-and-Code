package com.intimetec.newsaggregation.repository;

import com.intimetec.newsaggregation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query(value = "SELECT u FROM User u JOIN u.roles r WHERE r.roleName = 'ROLE_ADMIN'")
    Optional<User> findAdminUser();

}
