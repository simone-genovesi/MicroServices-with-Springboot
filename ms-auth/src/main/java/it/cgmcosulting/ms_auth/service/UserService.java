package it.cgmcosulting.ms_auth.service;

import it.cgmcosulting.ms_auth.entity.Role;
import it.cgmcosulting.ms_auth.entity.User;
import it.cgmcosulting.ms_auth.exception.GenericException;
import it.cgmcosulting.ms_auth.payload.request.SignupRequest;
import it.cgmcosulting.ms_auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<?> signup(SignupRequest request) {
        if(repo.existsByUsernameOrEmail(request.getUsername(), request.getEmail()))
            throw new GenericException("Username or email already in use");

        User user = User.builder()
                .email(request.getEmail())
                .enabled(true)
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.MEMBER)
                .build();
        repo.save(user);

        return ResponseEntity
                .status(201)
                .body(user);
    }
}
