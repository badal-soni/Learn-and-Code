package com.intimetec.newsaggregation.repository;

import com.intimetec.newsaggregation.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionsRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByPermissionName(String permission);

}
