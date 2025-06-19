package com.intimetec.newsaggregation.security;

import com.intimetec.newsaggregation.entity.Permission;
import com.intimetec.newsaggregation.entity.User;
import com.intimetec.newsaggregation.entity.UserRole;
import com.intimetec.newsaggregation.repository.PermissionsRepository;
import com.intimetec.newsaggregation.repository.UserRepository;
import com.intimetec.newsaggregation.repository.UserRolesRepository;
import com.intimetec.newsaggregation.util.JwtUtility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RequestInterceptor implements HandlerInterceptor {

    private final UserRolesRepository rolesRepository;
    private final PermissionsRepository permissionsRepository;
    private final JwtUtility jwtUtility;
    private final UserRepository userRepository;

    private static final String[] WHITE_LISTED_URLS = {
            "/api/v1/auth/"
    };

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {
        System.out.println("Request UDI: " + request.getRequestURI());
        for (String whiteListedUrl : WHITE_LISTED_URLS) {
            if (request.getRequestURI().startsWith(whiteListedUrl)) {
                System.out.println("White listed URL: " + request.getRequestURI());
                return true; // allow access to white listed URLs
            }
        }

        System.out.println("Pre handle invoked");
        if (handler instanceof HandlerMethod) {
            boolean ans = checkAccess(
                    request,
                    response,
                    (HandlerMethod) handler
            );
            System.out.println("Ans: " + ans);
//            return ans;
            return false;
        }
        System.out.println("false return");
        return false;
    }

    private boolean checkAccess(
            HttpServletRequest request,
            HttpServletResponse response,
            HandlerMethod handlerMethod
    ) {
        Method method = handlerMethod.getMethod();
        if (!method.isAnnotationPresent(Secured.class)) {
            return true; // accessible to everyone
        }
        String[] permissions = method.getAnnotation(Secured.class).value();
        System.out.println(Arrays.toString(permissions));
        for (String permission : permissions) {
            Optional<Permission> optionalPermission = permissionsRepository.findByPermissionName(permission);
            if (optionalPermission.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                System.out.println("Permission not found: " + permission);
                return false; // permission not found
            }
            Set<String> roles = optionalPermission.get().getRoles().stream().map(UserRole::getRoleName).collect(Collectors.toSet());
            String token = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
            String username = jwtUtility.verifyToken(token);
            Optional<User> user = userRepository.findByEmail(username);

            System.out.println("Permission roles: " + roles);
            System.out.println("Roles: " + user.get().getRoles());
            for (UserRole role: user.get().getRoles()) {
                List<Permission> perms = role.getPermissions();
                perms.forEach(perm -> {
                    System.out.println("User " + username + " has permission: " + perm.getPermissionName());
                });
                System.out.println("Checking role: " + role.getRoleName());
                if (roles.contains(role.getRoleName())) {
                    System.out.println("User " + username + " has access with role: " + role.getRoleName());
                    return true;
                }
            }
        }
        System.out.println("false from checkAccess");
        return false;
    }

}
