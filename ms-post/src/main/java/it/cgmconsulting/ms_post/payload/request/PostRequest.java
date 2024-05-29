package it.cgmconsulting.ms_post.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostRequest {

    @NotBlank @Size(min = 1, max = 100)
    private String title;

    @Size(max = 255)
    private String postImage;
}
