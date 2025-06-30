package com.intimetec.newsaggregation.seed;

import com.intimetec.newsaggregation.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRolesRepository userRolesRepository;
    private final PermissionsRepository permissionsRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final NewsCategoryRepository newsCategoryRepository;
    private final NotificationConfigurationRepository notificationConfigurationRepository;

    //
    @PostConstruct
    public void init() {

//        UserRole adminRole = new UserRole();
//        adminRole.setRoleName("ROLE_ADMIN");
////        adminRole.setPermissions(List.of(fullAccess));
//        adminRole = userRolesRepository.saveAndFlush(adminRole);
//
//        UserRole userRole = new UserRole();
//        userRole.setRoleName("USER");
////        userRole.setPermissions(List.of(readAccess));
//        userRole = userRolesRepository.saveAndFlush(userRole);
//
//        Permission fullAccess = new Permission();
//        fullAccess.setPermissionName("FullAccess");
//        fullAccess.setRoles(List.of(adminRole));
//
//        Permission readAccess = new Permission();
//        readAccess.setPermissionName("ReadAccess");
//        readAccess.setRoles(List.of(userRole));
//
//        fullAccess = permissionsRepository.saveAndFlush(fullAccess);
//        readAccess = permissionsRepository.saveAndFlush(readAccess);
//
//        User admin = new User();
//        admin.setEmail("soni.badal03@gmail.com");
//        admin.setPassword(passwordEncoder.encode("badal"));
//        admin.setUsername("soni.badal@gmail.com");
//        admin.setRoles(Set.of(adminRole));
//
//        admin = userRepository.saveAndFlush(admin);
//
//        List<NewsCategory> categories = new ArrayList<>();
//        NewsCategory generalCategory = new NewsCategory();
//        generalCategory.setCategoryName(PredefinedCategories.GENERAL);
//        categories.add(generalCategory);
//
//        NewsCategory businessCategory = new NewsCategory();
//        businessCategory.setCategoryName(PredefinedCategories.BUSINESS);
//        categories.add(businessCategory);
//
//        NewsCategory entertainmentCategory = new NewsCategory();
//        entertainmentCategory.setCategoryName(PredefinedCategories.ENTERTAINMENT);
//        categories.add(entertainmentCategory);
//
//        NewsCategory sportsCategory = new NewsCategory();
//        sportsCategory.setCategoryName(PredefinedCategories.SPORTS);
//        categories.add(sportsCategory);
//
//        NewsCategory technologyCategory = new NewsCategory();
//        technologyCategory.setCategoryName(PredefinedCategories.TECHNOLOGY);
//        categories.add(technologyCategory);
//
//        newsCategoryRepository.saveAllAndFlush(categories);

    }

}
