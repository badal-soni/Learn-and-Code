package com.intimetec.newsaggregation.seed;

import com.intimetec.newsaggregation.constant.Constants;
import com.intimetec.newsaggregation.constant.PredefinedCategories;
import com.intimetec.newsaggregation.entity.NewsCategory;
import com.intimetec.newsaggregation.entity.Permission;
import com.intimetec.newsaggregation.entity.User;
import com.intimetec.newsaggregation.entity.UserRole;
import com.intimetec.newsaggregation.repository.NewsCategoryRepository;
import com.intimetec.newsaggregation.repository.PermissionsRepository;
import com.intimetec.newsaggregation.repository.UserRepository;
import com.intimetec.newsaggregation.repository.UserRolesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@RequiredArgsConstructor
public class DataSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRolesRepository userRolesRepository;
    private final PermissionsRepository permissionsRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final NewsCategoryRepository newsCategoryRepository;

    @Value("${spring.mail.username}")
    private String adminEmail;

    @Value("${admin_password}")
    private String adminPassword;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (isDataAlreadyInitialized()) {
            return;
        }

        Permission fullAccess = createPermission(Constants.FULL_ACCESS_PERMISSION);
        Permission readAccess = createPermission(Constants.READ_ONLY_PERMISSION);

        UserRole adminRole = createRole(Constants.SPRING_ROLE_ADMIN, Collections.singletonList(fullAccess));
        createRole(Constants.ROLE_USER, Collections.singletonList(readAccess));

        createAdminUser(adminRole);
        createPredefinedCategories();
    }

    private boolean isDataAlreadyInitialized() {
        return userRolesRepository.count() > 0
                && permissionsRepository.count() > 0
                && userRepository.count() > 0
                && newsCategoryRepository.count() > 0;
    }

    private Permission createPermission(String permissionName) {
        Optional<Permission> existingPermission = permissionsRepository.findByPermissionName(permissionName);
        if (existingPermission.isPresent()) {
            return existingPermission.get();
        }

        Permission permission = new Permission();
        permission.setPermissionName(permissionName);
        return permissionsRepository.saveAndFlush(permission);
    }

    private UserRole createRole(String roleName, List<Permission> permissions) {
        Optional<UserRole> existingRole = userRolesRepository.findByRoleName(roleName);
        if (existingRole.isPresent()) {
            return existingRole.get();
        }

        UserRole role = new UserRole();
        role.setRoleName(roleName);
        role.setPermissions(permissions);
        return userRolesRepository.saveAndFlush(role);
    }

    private void createAdminUser(UserRole adminRole) {
        Optional<User> existingAdmin = userRepository.findByEmail(adminEmail);
        if (existingAdmin.isPresent()) {
            return;
        }

        User admin = new User();
        admin.setEmail(adminEmail);
        admin.setUsername(adminEmail);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRoles(Set.of(adminRole));
        userRepository.saveAndFlush(admin);
    }

    private void createPredefinedCategories() {
        List<String> categoryNames = Arrays.asList(
                PredefinedCategories.GENERAL,
                PredefinedCategories.BUSINESS,
                PredefinedCategories.ENTERTAINMENT,
                PredefinedCategories.SPORTS,
                PredefinedCategories.TECHNOLOGY
        );

        for (String categoryName : categoryNames) {
            if (!newsCategoryRepository.existsByCategoryName(categoryName)) {
                NewsCategory category = new NewsCategory();
                category.setCategoryName(categoryName);
                category.setHidden(false);
                newsCategoryRepository.saveAndFlush(category);
            }
        }
    }
}