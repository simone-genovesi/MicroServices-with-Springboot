package it.cgmconsulting.ms_comment.payload.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CommentRequest {

    @NotBlank @Size(min = 1, max = 255)
    private String comment;

    @Min(1)
    private int post;
}
