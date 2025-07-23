package com.sanjat.user_service.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.sanjat.user_service.dto.AuthRequest;
import com.sanjat.user_service.dto.AuthResponse;
import com.sanjat.user_service.dto.RegisterRequest;
import com.sanjat.user_service.dto.UserDto;
import com.sanjat.user_service.model.Role;
import com.sanjat.user_service.model.User;
import com.sanjat.user_service.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private PasswordEncoder passEncoder;
    private UserRepository repository;

    public AuthController(AuthenticationManager authManager, CustomUserDetailsService uds, JwtUtil jwtUtil,
            UserRepository repository,
            PasswordEncoder passEncoder) {
        this.authenticationManager = authManager;
        this.userDetailsService = uds;
        this.jwtUtil = jwtUtil;
        this.repository = repository;
        this.passEncoder = passEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        final String jwt = jwtUtil.generateToken((com.sanjat.user_service.security.CustomUserDetails) userDetails);

        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    /*
     * @PostMapping("/register")
     * public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest
     * request, BindingResult result) {
     * if (result.hasErrors()) {
     * Map<String, String> errors = new HashMap<>();
     * result.getFieldErrors().forEach(error -> {
     * errors.put(error.getField(), error.getDefaultMessage());
     * });
     * return ResponseEntity.badRequest().body(errors);
     * }
     * 
     * if (repository.findByUsername(request.getUsername()).isPresent()) {
     * return ResponseEntity.status(HttpStatus.CONFLICT)
     * .body("Korisnik sa ovim username-om već postoji!");
     * }
     * 
     * User user = new User();
     * user.setEmail(request.getEmail());
     * String hashedPassword = passEncoder.encode(request.getPassword());
     * user.setPassword(hashedPassword);
     * user.setUsername(request.getUsername());
     * user.setRole(com.sanjat.user_service.model.Role.STUDENT);
     * repository.save(user);
     * 
     * UserDto newUser = new UserDto();
     * newUser.setEmail(user.getEmail());
     * newUser.setUsername(user.getUsername());
     * return ResponseEntity.ok(newUser);
     * }
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return validationErrors(result);
        }

        return registerUser(request, Role.STUDENT);
    }

    @PostMapping("/register/professor")
    public ResponseEntity<?> registerProfessor(@Valid @RequestBody RegisterRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return validationErrors(result);
        }

        return registerUser(request, Role.PROFESSOR);
    }

    private ResponseEntity<?> validationErrors(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    private ResponseEntity<?> registerUser(RegisterRequest request, Role role) {
        if (repository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Korisnik sa ovim username-om već postoji!");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passEncoder.encode(request.getPassword()));
        user.setRole(role);
        repository.save(user);

        UserDto newUser = new UserDto();
        newUser.setEmail(user.getEmail());
        newUser.setUsername(user.getUsername());
        return ResponseEntity.ok(newUser);
    }

}
