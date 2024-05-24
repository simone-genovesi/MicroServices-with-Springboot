package it.cgmcosulting.ms_auth.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SigninResponse {

    private String username;
    private String role;
    private String token;
}
