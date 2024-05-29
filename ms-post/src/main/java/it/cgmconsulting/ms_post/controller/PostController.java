package it.cgmconsulting.ms_post.controller;

import it.cgmconsulting.ms_post.payload.request.PostRequest;
import it.cgmconsulting.ms_post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/v2")
    public ResponseEntity<?> createPost(
            @RequestBody @Valid PostRequest request,
            @RequestHeader("userId") int author){
        return postService.createPost(request, author);
    }
}
