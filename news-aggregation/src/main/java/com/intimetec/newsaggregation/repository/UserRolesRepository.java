package com.intimetec.newsaggregation.repository;

import com.intimetec.newsaggregation.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRole, Long> {

    Optional<UserRole> findByRoleName(String userRole);

}
