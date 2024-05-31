package it.cgmconsulting.ms_auth.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class SimpleUserResponse {

    private int id;
    private String username;
}
