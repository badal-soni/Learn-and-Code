package com.intimetec.newsaggregation.service.impl;

import com.intimetec.newsaggregation.constant.Constants;
import com.intimetec.newsaggregation.dto.event.UserRegisteredEvent;
import com.intimetec.newsaggregation.dto.request.SignInRequest;
import com.intimetec.newsaggregation.dto.request.SignUpRequest;
import com.intimetec.newsaggregation.dto.response.SignInResponse;
import com.intimetec.newsaggregation.dto.response.SignUpResponse;
import com.intimetec.newsaggregation.entity.User;
import com.intimetec.newsaggregation.entity.UserRole;
import com.intimetec.newsaggregation.exception.NotFoundException;
import com.intimetec.newsaggregation.repository.UserRepository;
import com.intimetec.newsaggregation.repository.UserRolesRepository;
import com.intimetec.newsaggregation.service.AuthService;
import com.intimetec.newsaggregation.util.JwtUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserRolesRepository userRolesRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtility jwtUtility;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public SignUpResponse signUpUser(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new NotFoundException("Email already exists");
        }

        Optional<UserRole> userRole = userRolesRepository.findByRoleName(Constants.ROLE_USER);
        if (userRole.isEmpty()) {
            throw new NotFoundException("Role USER not found");
        }
        User user = User.builder()
                .email(signUpRequest.getEmail())
                .username(signUpRequest.getUsername())
                .password(bCryptPasswordEncoder.encode(signUpRequest.getPassword()))
                .roles(Set.of(userRole.get()))
                .build();
        user = userRepository.saveAndFlush(user);
        SignUpResponse signUpResponse = new SignUpResponse();
        signUpResponse.setId(user.getId());
        publishUserRegisteredEvent(user);
        return signUpResponse;
    }

    private void publishUserRegisteredEvent(User user) {
        UserRegisteredEvent userRegisteredEvent = new UserRegisteredEvent();
        userRegisteredEvent.setUserId(user.getId());
        userRegisteredEvent.setEmail(user.getEmail());
        userRegisteredEvent.setUsername(user.getUsername());
        applicationEventPublisher.publishEvent(userRegisteredEvent);
    }

    @Override
    public SignInResponse signInUser(SignInRequest signInRequest) {
        User user = userRepository
                .findByEmail(signInRequest.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found with email: " + signInRequest.getEmail()));

        if (!bCryptPasswordEncoder.matches(signInRequest.getPassword(), user.getPassword())) {
            throw new NotFoundException("Invalid credentials");
        }

        final String accessToken = jwtUtility.generateToken(signInRequest.getEmail());
        return new SignInResponse(accessToken, user.getRoles().stream().map(UserRole::getRoleName).collect(Collectors.toSet()));
    }

}
