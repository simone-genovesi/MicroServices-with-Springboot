package it.cgmconsulting.ms_comment.controller;

import it.cgmconsulting.ms_comment.payload.request.CommentRequest;
import it.cgmconsulting.ms_comment.service.CommentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/v3")
    public ResponseEntity<?> createComment(
            @RequestBody @Valid CommentRequest request,
            @RequestHeader("userId") int author
            ){
        return commentService.createComment(request, author);
    }

    @GetMapping("/v0/{id}")
    public ResponseEntity<?> getComment(@PathVariable @Min(1) int id){
        return commentService.getComment(id);
    }


}
