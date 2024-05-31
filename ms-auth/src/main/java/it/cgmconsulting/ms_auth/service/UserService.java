package it.cgmconsulting.ms_auth.service;

import it.cgmconsulting.ms_auth.entity.Role;
import it.cgmconsulting.ms_auth.entity.User;
import it.cgmconsulting.ms_auth.exception.GenericException;
import it.cgmconsulting.ms_auth.payload.request.SigninRequest;
import it.cgmconsulting.ms_auth.payload.response.SigninResponse;
import it.cgmconsulting.ms_auth.payload.response.SimpleUserResponse;
import it.cgmconsulting.ms_auth.utils.Consts;
import it.cgmconsulting.ms_auth.exception.ResourceNotFoundException;
import it.cgmconsulting.ms_auth.payload.request.SignupRequest;
import it.cgmconsulting.ms_auth.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    @Value("${application.security.internalToken}")
    String internalToken;

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
        log.info("Signup : {} " , user );

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
        SigninResponse response = new SigninResponse(user.getUsername(), user.getRole().name(), jwt);

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
            if(role.equals("WRITER")){
                // aggiorno mappa getWriters su ms-post
                ResponseEntity<?> r = sendNeWriter(user.getId(), user.getUsername());
                if(r.getStatusCode() != HttpStatus.OK)
                    return ResponseEntity.status(503).body("Change role to WRITER failed");
            }
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Role has been updated from user " + user.getUsername());
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Same role");
    }

    public String getUsername(int userId) {
        String username = repo.getUsername(userId);
        return username != null ? username :"anonymous";
    }

    public Map<Integer, String> getUsernames(String role) {
        Set<SimpleUserResponse> set = repo.getSimpleUsers(Role.valueOf(role.toUpperCase()));
        return set.stream().collect(Collectors.toMap(SimpleUserResponse::getId, SimpleUserResponse::getUsername));
    }

    private ResponseEntity<?> sendNeWriter(int userId, String username){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization-Internal", internalToken);

        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);

        String url = Consts.GATEWAY+"/"+Consts.MS_POST+"/v99/writers?id={userId}&username={username}";

        ResponseEntity<String> r = null;
        try{
            r = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class, userId, username);
        } catch (RestClientException e){
            log.error(e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
        return r;
    }

}
