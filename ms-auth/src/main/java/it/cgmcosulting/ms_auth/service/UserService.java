package it.cgmcosulting.ms_auth.service;

import it.cgmcosulting.ms_auth.entity.Role;
import it.cgmcosulting.ms_auth.entity.User;
import it.cgmcosulting.ms_auth.exception.GenericException;
import it.cgmcosulting.ms_auth.exception.ResourceNotFoundException;
import it.cgmcosulting.ms_auth.payload.request.SigninRequest;
import it.cgmcosulting.ms_auth.payload.request.SignupRequest;
import it.cgmcosulting.ms_auth.payload.response.SigninResponse;
import it.cgmcosulting.ms_auth.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public ResponseEntity<?> signup(SignupRequest request) {
        if(repo.existsByUsernameOrEmail(request.getUsername(), request.getEmail()))
            throw new GenericException("Username or email already in use", HttpStatus.CONFLICT);

        User user = User.builder()
                .email(request.getEmail())
                .enabled(true)
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.MEMBER)
                .createdAt(LocalDateTime.now())
                .build();
        repo.save(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(user);
    }

    public ResponseEntity<?> signin(SigninRequest request) {
        User user = repo.findByUsername(request.getUsername())
                .orElseThrow(() -> new GenericException("Bad credentials", HttpStatus.BAD_REQUEST));
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new GenericException("Bad credentials", HttpStatus.BAD_REQUEST);
        if(!user.isEnabled())
            throw new GenericException("Account not enabled", HttpStatus.FORBIDDEN);

        String jwt = jwtService.generateToken(user);
        SigninResponse response = new SigninResponse(user.getUsername(), user.getRole().toString(), jwt);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Transactional
    public ResponseEntity<?> changeRole(int userId, String role, int id) {
        User user = repo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        if (id == user.getId())
            throw new GenericException("You cannot change role to yourself", HttpStatus.FORBIDDEN);

        if (!user.getRole().name().equals(role)){
            user.setRole(Role.valueOf(role.toUpperCase()));
            user.setUpdatedAt(LocalDateTime.now());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Role has been updated fro user " + user.getUsername());
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Same role");


    }
}
