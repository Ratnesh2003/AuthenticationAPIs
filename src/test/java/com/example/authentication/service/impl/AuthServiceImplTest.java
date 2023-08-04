package com.example.authentication.service.impl;

import com.example.authentication.exceptions.AlreadyExistsException;
import com.example.authentication.exceptions.RoleDoesNotExistException;
import com.example.authentication.model.Role;
import com.example.authentication.model.Token;
import com.example.authentication.model.User;
import com.example.authentication.payload.request.LoginReq;
import com.example.authentication.payload.request.RegisterReq;
import com.example.authentication.repository.RoleRepo;
import com.example.authentication.repository.TokenRepo;
import com.example.authentication.repository.UserRepo;
import com.example.authentication.service.jwt.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private RoleRepo roleRepo;

    @Mock
    private TokenRepo tokenRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;
    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testSignup_Success() throws RoleDoesNotExistException, AlreadyExistsException {
        // Mocking the registerReq object
        RegisterReq registerReq = new RegisterReq();
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        registerReq.setEmail("abcd@efgh.com");
        registerReq.setUsername("random_user");
        registerReq.setPassword("Abcd@1234");
        registerReq.setRoles(roles);

        // when
        // Mocking role existence check
        when(roleRepo.existsRoleByNameIgnoreCase(any())).thenReturn(true);
        when(roleRepo.findByNameIgnoreCase(any())).thenReturn(new Role("ROLE_USER"));

        // Mocking user existence checks
        when(userRepo.existsByEmailIgnoreCase(any())).thenReturn(false);
        when(userRepo.existsByUsernameIgnoreCase(any())).thenReturn(false);

        // Mocking password encoding
        when(passwordEncoder.encode(any())).thenReturn("encoded_password");

        // Mocking user saving
        when(userRepo.save(any())).thenReturn(new User());
        
        // then

        // Calling the method
        authService.signup(registerReq);

        // Verifying that the save method was called
        verify(userRepo, times(1)).save(any());
    }

    @Test
    public void testSignup_AlreadyExistsException() {
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        // Mocking the registerReq object
        RegisterReq registerReq = new RegisterReq();
        registerReq.setEmail("abcd@efgh.com");
        registerReq.setUsername("random_user");
        registerReq.setPassword("Abcd@1234");
        registerReq.setRoles(roles);

        // Mocking user existence checks to return true
        when(userRepo.existsByEmailIgnoreCase(any())).thenReturn(true);
        when(userRepo.existsByUsernameIgnoreCase(any())).thenReturn(true);

        // Calling the method and asserting the exception
        assertThrows(AlreadyExistsException.class, () -> authService.signup(registerReq));
    }

    @Test
    public void testSignup_RoleDoesNotExistException() {
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_RANDOM");
        // Mocking the registerReq object
        RegisterReq registerReq = new RegisterReq();
        registerReq.setEmail("abcd@efgh.com");
        registerReq.setUsername("random_user");
        registerReq.setPassword("Abcd@1234");
        registerReq.setRoles(roles);

        // Mocking role existence check to return false
        when(roleRepo.existsRoleByNameIgnoreCase(any())).thenReturn(false);

        // Calling the method and asserting the exception
        assertThrows(RoleDoesNotExistException.class, () -> authService.signup(registerReq));
    }

    @Test
    public void testLogin_Success() {
        // Mocking the loginReq object
        LoginReq loginReq = new LoginReq("abcd@efgh.com", "Abcd@1234");

        // Mocking user details
        User user = new User();
        user.setId(1L);
        user.setUsername("random_user");
        user.setEmail("abcd@efgh.com");
        user.setPassword("encoded_password");

        // Mocking user existence check
        when(userDetailsService.loadUserByUsername(any())).thenReturn(user);

        // Mocking password matching
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        // Mocking JWT token generation
        when(jwtUtil.generateToken(any())).thenReturn("mocked_token");

        // Mocking role retrieval
        Role userRole = new Role("ROLE_USER");
        when(roleRepo.findByNameIgnoreCase(any())).thenReturn(userRole);
        user.setRoles(new HashSet<>(Set.of(userRole)));

        // Mocking token saving
        when(tokenRepo.existsByEmail(any())).thenReturn(false);
        when(tokenRepo.save(any())).thenReturn(new Token("abcd@efgh.com", "mocked_token", new Date(System.currentTimeMillis())));

        // Calling the method
        ResponseEntity<?> responseEntity = authService.login(loginReq);

        // Verifying the response status and headers
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("mocked_token", responseEntity.getHeaders().getFirst("Set-Cookie"));
    }

    @Test
    public void testLogin_IncorrectPassword() {
        // Mocking the loginReq object
        LoginReq loginReq = new LoginReq("abcd@efgh.com", "Abcd@1234");

        // Mocking user details
        User user = new User();
        user.setId(1L);
        user.setUsername("random_user");
        user.setEmail("abcd@efgh.com");
        user.setPassword("encoded_password");

        // Mocking user existence check
        when(userDetailsService.loadUserByUsername(any())).thenReturn(user);

        // Mocking password matching to return false
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        // Calling the method and asserting the response status
        ResponseEntity<?> responseEntity = authService.login(loginReq);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }



}
