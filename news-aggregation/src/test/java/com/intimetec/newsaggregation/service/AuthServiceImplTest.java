package com.intimetec.newsaggregation.service;

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
import com.intimetec.newsaggregation.service.impl.AuthServiceImpl;
import com.intimetec.newsaggregation.util.JwtUtility;
import com.intimetec.newsaggregation.util.MockDataCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRolesRepository userRolesRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtUtility jwtUtility;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Captor
    private ArgumentCaptor<UserRegisteredEvent> eventCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignUpUser_success() {
        SignUpRequest request = MockDataCreator.createMockSignUpRequestData();
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);

        UserRole role = new UserRole();
        role.setId(1L);
        role.setRoleName(Constants.ROLE_USER);
        role.setPermissions(List.of());
        when(userRolesRepository.findByRoleName(Constants.ROLE_USER)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashed_password");

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password("hashed_password")
                .roles(Set.of(role))
                .build();
        user.setId(1L);
        when(userRepository.saveAndFlush(any(User.class))).thenReturn(user);

        SignUpResponse response = authService.signUpUser(request);

        assertEquals(1L, response.getId());
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        UserRegisteredEvent event = eventCaptor.getValue();
        assertEquals("john@example.com", event.getEmail());
    }

    @Test
    void testSignUpUser_emailExists_shouldThrowException() {
        SignUpRequest request = MockDataCreator.createMockSignUpRequestData();
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        Assertions.assertThrows(NotFoundException.class, () -> authService.signUpUser(request));
    }

    @Test
    void testSignUpUser_roleNotFound_shouldThrowException() {
        SignUpRequest request = MockDataCreator.createMockSignUpRequestData();
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRolesRepository.findByRoleName(Constants.ROLE_USER)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> authService.signUpUser(request));
    }

    @Test
    void testSignInUser_success() {
        SignInRequest request = MockDataCreator.createMockSignInRequestData();

        UserRole role = new UserRole();
        role.setId(1L);
        role.setRoleName(Constants.ROLE_USER);
        User user = User.builder()
                .email(request.getEmail())
                .password("hashed_password")
                .roles(Set.of(role))
                .build();
        user.setId(1L);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), "hashed_password")).thenReturn(true);
        when(jwtUtility.generateToken(request.getEmail())).thenReturn("mocked_token");

        SignInResponse response = authService.signInUser(request);
        assertEquals("mocked_token", response.jwtToken());
        assertTrue(response.roles().contains(Constants.ROLE_USER));
    }

    @Test
    void testSignInUser_userNotFound_shouldThrowException() {
        SignInRequest request = MockDataCreator.createMockSignInRequestData();
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> authService.signInUser(request));
    }

    @Test
    void testSignInUser_invalidPassword_shouldThrowException() {
        SignInRequest request = MockDataCreator.createMockSignInRequestData();

        User user = User.builder()
                .email("john@example.com")
                .password("correct_hashed_password")
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), "correct_hashed_password")).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> authService.signInUser(request));
    }
}