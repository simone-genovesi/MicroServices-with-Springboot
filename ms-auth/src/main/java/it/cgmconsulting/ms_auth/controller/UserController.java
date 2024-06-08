package it.cgmconsulting.ms_auth.controller;

import it.cgmconsulting.ms_auth.payload.request.SigninRequest;
import it.cgmconsulting.ms_auth.payload.request.SignupRequest;
import it.cgmconsulting.ms_auth.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/v0/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequest request){
        return service.signup(request);
    }

    @PostMapping("/v0/signin")
    public ResponseEntity<?> signin(@RequestBody @Valid SigninRequest request){
        return service.signin(request);
    }

    @PutMapping("/v1/{userId}")
    public ResponseEntity<?> changeRole(
            @PathVariable int userId,
            @RequestParam String role,
            @RequestHeader("userId") int id
    ){
        return service.changeRole(userId, role, id);
    }

    @GetMapping("/v99/{userId}")
    public String getUsername(@PathVariable @Min(1) int userId){
        return service.getUsername(userId);
    }

    @PostMapping("/v99/role")
    public Map<Integer,String> getUsernames(@RequestBody Set<Integer> authorIds) /*throws InterruptedException*/ {
        //Thread.sleep(3000);
        return service.getUsernames(authorIds);
    }
}
